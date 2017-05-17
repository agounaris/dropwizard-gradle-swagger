package com.sample.app;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.sample.app.auth.ApiAuthenticator;
import com.sample.app.auth.ApiAuthorizer;
import com.sample.app.auth.User;
import com.sample.app.core.Post;
import com.sample.app.dao.PostDao;
import com.sample.app.filter.LogUserActionFilter;
import com.sample.app.health.WebApiCheck;
import com.sample.app.resources.SampleResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;


public class SampleApplication extends Application<SampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "sample-app";
    }

    private final SwaggerBundle<SampleConfiguration> swaggerBundle = new SwaggerBundle<SampleConfiguration>() {
        @Override
        protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(SampleConfiguration configuration) {
            return configuration.getSwaggerBundleConfiguration();
        }
    };

    private final HibernateBundle<SampleConfiguration> hibernateBundle = new HibernateBundle<SampleConfiguration>(Post.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(SampleConfiguration configuration) {
            return configuration.getDatabase();
        }

        // Register hibernate interceptor to set the created and modified dates when persisting
        protected void configure(org.hibernate.cfg.Configuration configuration) {
//            configuration.setInterceptor(new EntityDateTimeInterceptor(BaseEntity.class));
        }
    };

    private final MigrationsBundle<SampleConfiguration> migrationsBundle = new MigrationsBundle<SampleConfiguration>() {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(SampleConfiguration configuration) {
            return configuration.getDatabase();
        }
    };

    @Override
    public void initialize(final Bootstrap<SampleConfiguration> bootstrap) {
        bootstrap.addBundle(swaggerBundle);
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(migrationsBundle);
    }

    @Override
    public void run(SampleConfiguration configuration,
                    Environment environment) {
        environment.jersey()
                .register(new AuthDynamicFeature(
                        new BasicCredentialAuthFilter.Builder<User>().setAuthenticator(new ApiAuthenticator(configuration.getUsers()))
                                .setAuthorizer(new ApiAuthorizer()).setRealm("PROTECTED").buildAuthFilter()));

        final WebApiCheck healthCheck = new WebApiCheck(configuration.getAppVersion());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(healthCheck);

        Injector injector = createInjector(configuration);

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(injector.getInstance(RolesAllowedDynamicFeature.class));
        environment.jersey().register(injector.getInstance(SampleResource.class));
        environment.jersey().register(injector.getInstance(LogUserActionFilter.class));
    }

    private Injector createInjector(final SampleConfiguration configuration) {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(SessionFactory.class).toInstance(hibernateBundle.getSessionFactory());
                bind(PostDao.class).in(Singleton.class);
            }
        });
    }

}
