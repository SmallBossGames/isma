package ru.nstu.isma.app.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.beans.factory.BeanFactoryUtils.beanOfTypeIncludingAncestors;


@Service
public final class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        CONTEXT = applicationContext;
    }

    public static ApplicationContext context() {
        return CONTEXT;
    }

    public static <T> T autowire(T targetBean) {
        if (targetBean == null) {
            return null;
        }
        if (CONTEXT != null) {
            CONTEXT.getAutowireCapableBeanFactory().autowireBean(targetBean);
        }
        if (targetBean instanceof InitializingBean)
            try {
                ((InitializingBean) targetBean).afterPropertiesSet();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        return targetBean;
    }

    public static <T> T getBean(Class<T> clazz) {
        return beanOfType(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return CONTEXT == null ? null : (T) CONTEXT.getBean(beanName);
    }

    private static <T> T beanOfType(Class<T> clazz) {
        try {
            return (T) beanOfTypeIncludingAncestors(CONTEXT, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return CONTEXT.getBeansOfType(type);
    }

    public static <T> T createBean(Class<T> bean) {
        return CONTEXT.getAutowireCapableBeanFactory().createBean(bean);
    }
}
