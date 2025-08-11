package com.phuonghn.pkm.common;

public class Constants {
    public static final String CREATED_DATE = "AUDIT.CREATED_DATE";
    public static final String LAST_MODIFIED_DATE = "AUDIT.LAST_MODIFIED_DATE";
    public static final String CREATED_BY = "AUDIT.CREATED_BY";
    public static final String LAST_MODIFIED_BY = "AUDIT.LAST_MODIFIED_BY";

    public static final class KAFKA_TOPIC {
        public static final String PKM_SEARCH = "pokemon-search";
        public static final String PKM_NEWS = "pokemon-news";
    }

    public static final class KAFKA_GROUP {
        public static final String PKM_NEWS = "news-group";
    }

    public static final class EVOLUTION_TYPE {
        public static final String NORMAL = "NORMAL";
        public static final String MEGA = "MEGA";
    }

    public static final class GLOBAL_PARAM_TYPE {
        public static final String GENERATION = "GENERATION";
    }
}
