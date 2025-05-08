package com.acroninspector.app.di.global;

import android.content.Context;
import com.acroninspector.app.di.app.AppComponent;
import com.acroninspector.app.di.app.AppModule;
import com.acroninspector.app.di.app.DaggerAppComponent;
import com.acroninspector.app.di.global.base.BaseComponent;
import com.acroninspector.app.di.global.base.BaseComponentBuilder;
import com.acroninspector.app.di.global.base.BaseModule;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ComponentHolder {

    private final Context context;

    @Inject
    Map<Class<?>, Provider<BaseComponentBuilder>> builders;

    private Map<Class<?>, BaseComponent> components;

    public ComponentHolder(Context context) {
        this.context = context;
    }

    public void inject() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context)).build();
        appComponent.injectComponentsHolder(this);
        components = new HashMap<>();
    }

    public BaseComponent getComponent(Class<?> cls, BaseModule module) {
        BaseComponent component = components.get(cls);
        if (component == null) {
            BaseComponentBuilder builder = Objects.requireNonNull(builders.get(cls)).get();
            if (module != null) {
                builder.module(module);
            }
            component = builder.build();
            components.put(cls, component);
        }
        return component;
    }

    public BaseComponent getComponent(Class<?> cls) {
        return getComponent(cls, null);
    }

    public void releaseComponent(Class<?> cls) {
        components.put(cls, null);
    }
}
