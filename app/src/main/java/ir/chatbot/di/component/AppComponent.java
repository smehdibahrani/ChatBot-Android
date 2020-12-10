package ir.chatbot.di.component;

import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import ir.chatbot.App;
import ir.chatbot.di.builder.ActivityBuilder;
import ir.chatbot.di.module.AppModule;
import ir.chatbot.di.builder.ServiceBuilderModule;
import ir.chatbot.rt.MyService;

import javax.inject.Singleton;


@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        ServiceBuilderModule.class,
        AppModule.class,
        ActivityBuilder.class})

public interface AppComponent {

    void inject(App app);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
