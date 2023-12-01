package system.core.service.schedule.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.springframework.stereotype.Component;
import system.core.enums.schedule.JobClassType;
import system.core.exception.JobException;
import system.share.schedule.JobBeanProperties;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobClassLoader {
    private final JobBeanProperties jobBeanProperties;

    /**
     * 모듈별 작업 대상 클래스를 가져온다.
     */
    public Class<? extends Job> get(String className, JobClassType classType) throws JobException {
        if (StringUtils.isEmpty(className)) throw JobException.jobClassNotFound();

        return switch (classType) {
            case LOCAL -> getClass(className, jobBeanProperties.getLocal());
            case REMOTE -> getClass(className, jobBeanProperties.getRemote());
        };
    }

    /**
     * 작업 대상 클래스를 가져온다.
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Job> getClass(String className, String classLocation) throws JobException {
        try {
            return (Class<? extends Job>) createClassLoader(classLocation).loadClass(className);
        } catch (ClassNotFoundException e) {
            throw JobException.jobClassNotFound();
        }
    }

    /**
     * 작업 대상 클래스를 위한 로더를 생성한다.
     */
    private ClassLoader createClassLoader(String classLocation) throws JobException {
        try {
            File classFile = new File(classLocation);
            ClassLoader classLoader = this.getClass().getClassLoader();
            return new URLClassLoader(new URL[]{classFile.toURI().toURL()}, classLoader);
        } catch (IOException e) {
            throw JobException.jobClassNotFound();
        }
    }

    /**
     * 작업 대상 클래스를 위한 로더를 생성한다.
     */
    private ClassLoader createClassLoader() throws JobException {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            return new URLClassLoader(new URL[]{
                   new File(jobBeanProperties.getLocal()).toURI().toURL(),
                   new File(jobBeanProperties.getRemote()).toURI().toURL(),
            }, classLoader);
        } catch (IOException e) {
            throw JobException.jobClassNotFound();
        }
    }
}