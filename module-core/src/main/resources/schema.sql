DROP TABLE IF EXISTS quartz_fired_triggers;
DROP TABLE IF EXISTS quartz_paused_trigger_grps;
DROP TABLE IF EXISTS quartz_scheduler_state;
DROP TABLE IF EXISTS quartz_locks;
DROP TABLE IF EXISTS quartz_simple_triggers;
DROP TABLE IF EXISTS quartz_cron_triggers;
DROP TABLE IF EXISTS quartz_triggers;
DROP TABLE IF EXISTS quartz_job_details;

CREATE TABLE IF NOT EXISTS quartz_job_details (
  sched_name        VARCHAR(120) NOT NULL,
  job_name          VARCHAR(200) NOT NULL,
  job_group         VARCHAR(200) NOT NULL,
  description       VARCHAR(250) NULL,
  job_class_name    VARCHAR(250) NOT NULL,
  is_durable        VARCHAR(1) NOT NULL,
  is_nonconcurrent  VARCHAR(1) NOT NULL,
  is_update_data    VARCHAR(1) NOT NULL,
  requests_recovery VARCHAR(1) NOT NULL,
  job_data          BLOB NULL,
  PRIMARY KEY (sched_name, job_name, job_group)
);

CREATE TABLE IF NOT EXISTS quartz_triggers (
  sched_name     VARCHAR(120) NOT NULL,
  trigger_name   VARCHAR(200) NOT NULL,
  trigger_group  VARCHAR(200) NOT NULL,
  job_name       VARCHAR(200) NOT NULL,
  job_group      VARCHAR(200) NOT NULL,
  description    VARCHAR(250) NULL,
  next_fire_time BIGINT(13) NULL,
  prev_fire_time BIGINT(13) NULL,
  priority       INT NULL,
  trigger_state  VARCHAR(16) NOT NULL,
  trigger_type   VARCHAR(8) NOT NULL,
  start_time     BIGINT(13) NOT NULL,
  end_time       BIGINT(13) NULL,
  calendar_name  VARCHAR(200) NULL,
  misfire_instr  SMALLINT(2) NULL,
  job_data       BLOB NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, job_name, job_group)
  REFERENCES quartz_job_details (sched_name, job_name, job_group)
);

CREATE TABLE IF NOT EXISTS quartz_simple_triggers (
  sched_name      VARCHAR(120) NOT NULL,
  trigger_name    VARCHAR(200) NOT NULL,
  trigger_group   VARCHAR(200) NOT NULL,
  repeat_count    BIGINT(7) NOT NULL,
  repeat_interval BIGINT(12) NOT NULL,
  times_triggered BIGINT(10) NOT NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES quartz_triggers (sched_name, trigger_name, trigger_group)
);

CREATE TABLE IF NOT EXISTS quartz_cron_triggers (
  sched_name      VARCHAR(120) NOT NULL,
  trigger_name    VARCHAR(200) NOT NULL,
  trigger_group   VARCHAR(200) NOT NULL,
  cron_expression VARCHAR(200) NOT NULL,
  time_zone_id    VARCHAR(80),
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES quartz_triggers (sched_name, trigger_name, trigger_group)
);

CREATE TABLE IF NOT EXISTS quartz_paused_trigger_grps (
  sched_name    VARCHAR(120) NOT NULL,
  trigger_group VARCHAR(200) NOT NULL,
  PRIMARY KEY (sched_name, trigger_group)
);

CREATE TABLE IF NOT EXISTS quartz_fired_triggers (
  sched_name        VARCHAR(120) NOT NULL,
  entry_id          VARCHAR(95) NOT NULL,
  trigger_name      VARCHAR(200) NOT NULL,
  trigger_group     VARCHAR(200) NOT NULL,
  instance_name     VARCHAR(200) NOT NULL,
  fired_time        BIGINT(13) NOT NULL,
  sched_time        BIGINT(13) NOT NULL,
  priority          INT NOT NULL,
  state             VARCHAR(16) NOT NULL,
  job_name          VARCHAR(200) NULL,
  job_group         VARCHAR(200) NULL,
  is_nonconcurrent  VARCHAR(1) NULL,
  requests_recovery VARCHAR(1) NULL,
  PRIMARY KEY (sched_name, entry_id)
);

CREATE TABLE IF NOT EXISTS quartz_scheduler_state (
  sched_name        VARCHAR(120) NOT NULL,
  instance_name     VARCHAR(200) NOT NULL,
  last_checkin_time BIGINT(13) NOT NULL,
  checkin_interval  BIGINT(13) NOT NULL,
  PRIMARY KEY (sched_name, instance_name)
);

CREATE TABLE IF NOT EXISTS quartz_locks (
  sched_name VARCHAR(120) NOT NULL,
  lock_name  VARCHAR(40) NOT NULL,
  PRIMARY KEY (sched_name, lock_name)
);

CREATE INDEX IF NOT EXISTS idx_quartz_j_req_recovery ON quartz_job_details (sched_name, requests_recovery);
CREATE INDEX IF NOT EXISTS idx_quartz_j_grp ON quartz_job_details (sched_name, job_group);

CREATE INDEX IF NOT EXISTS idx_quartz_t_j ON quartz_triggers (sched_name, job_name, job_group);
CREATE INDEX IF NOT EXISTS idx_quartz_t_jg ON quartz_triggers (sched_name, job_group);
CREATE INDEX IF NOT EXISTS idx_quartz_t_c ON quartz_triggers (sched_name, calendar_name);
CREATE INDEX IF NOT EXISTS idx_quartz_t_g ON quartz_triggers (sched_name, trigger_group);
CREATE INDEX IF NOT EXISTS idx_quartz_t_state ON quartz_triggers (sched_name, trigger_state);
CREATE INDEX IF NOT EXISTS idx_quartz_t_n_state ON quartz_triggers (sched_name, trigger_name, trigger_group, trigger_state);
CREATE INDEX IF NOT EXISTS idx_quartz_t_n_g_state ON quartz_triggers (sched_name, trigger_group, trigger_state);
CREATE INDEX IF NOT EXISTS idx_quartz_t_next_fire_time ON quartz_triggers (sched_name, next_fire_time);
CREATE INDEX IF NOT EXISTS idx_quartz_t_nft_st ON quartz_triggers (sched_name, trigger_state, next_fire_time);
CREATE INDEX IF NOT EXISTS idx_quartz_t_nft_misfire ON quartz_triggers (sched_name, misfire_instr, next_fire_time);
CREATE INDEX IF NOT EXISTS idx_quartz_t_nft_st_misfire ON quartz_triggers (sched_name, misfire_instr, next_fire_time, trigger_state);
CREATE INDEX IF NOT EXISTS idx_quartz_t_nft_st_misfire_grp ON quartz_triggers (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);

CREATE INDEX IF NOT EXISTS idx_quartz_ft_trig_inst_name ON quartz_fired_triggers (sched_name, instance_name);
CREATE INDEX IF NOT EXISTS idx_quartz_ft_inst_job_req_rcvry ON quartz_fired_triggers (sched_name, instance_name, requests_recovery);
CREATE INDEX IF NOT EXISTS idx_quartz_ft_j_g ON quartz_fired_triggers (sched_name, job_name, job_group);
CREATE INDEX IF NOT EXISTS idx_quartz_ft_jg ON quartz_fired_triggers (sched_name, job_group);
CREATE INDEX IF NOT EXISTS idx_quartz_ft_t_g ON quartz_fired_triggers (sched_name, trigger_name, trigger_group);
CREATE INDEX IF NOT EXISTS idx_quartz_ft_tg ON quartz_fired_triggers (sched_name, trigger_group);

COMMIT;