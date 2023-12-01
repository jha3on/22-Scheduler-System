package system.share.base.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import system.core.enums.schedule.JobClassType;
import system.core.enums.schedule.TriggerClassType;
import system.core.enums.schedule.TriggerPolicyType;
import system.core.enums.search.JobSortBy;
import system.core.enums.search.TriggerSortBy;
import system.core.enums.search.UserSortBy;
import system.share.base.common.enums.SortType;

@Slf4j
public class TypeConverter {
    public static class SortConverter implements Converter<String, SortType> {
        public SortType convert(String name) {
            return SortType.getByName(name);
        }
    }

    public static class UserSortConverter implements Converter<String, UserSortBy> {
        public UserSortBy convert(String name) {
            return UserSortBy.getByName(name);
        }
    }


    public static class JobSortConverter implements Converter<String, JobSortBy> {
        public JobSortBy convert(String name) {
            return JobSortBy.getByName(name);
        }
    }

    public static class JobClassConverter implements Converter<String, JobClassType> {
        public JobClassType convert(String name) {
            return JobClassType.getByName(name);
        }
    }

    public static class TriggerSortConverter implements Converter<String, TriggerSortBy> {
        public TriggerSortBy convert(String name) {
            return TriggerSortBy.getByName(name);
        }
    }

    public static class TriggerClassConverter implements Converter<String, TriggerClassType> {
        public TriggerClassType convert(String name) {
            return TriggerClassType.getByName(name);
        }
    }

    public static class TriggerPolicyConverter implements Converter<String, TriggerPolicyType> {
        public TriggerPolicyType convert(String name) {
            return TriggerPolicyType.getByName(name);
        }
    }
}