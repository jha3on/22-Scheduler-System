let schedule = {
  init: function () {
    let _this = this;
    console.log('schedule.init()');
  },

  onJobStoreHandler: function () {
    app.clearCheckMessage();

    const payload = {
      jobName: $('#jobName').val(),
      jobGroup: $('#jobGroup').val(),
      className: $('#className').val(),
      classData: $('#classData').val(),
      classComment: $('#classComment').val(),
      classType: $('#classType').val(),
    };

    $.ajax({
      url: '/api/jobs',
      type: 'post',
      contentType: 'application/json; charset=utf-8;',
      dataType: 'json',
      data: JSON.stringify(payload)
    }).done(function ({ payload }) {
      alert('작업을 등록하였습니다.');
    }).fail(function ({ responseJSON }) {
      schedule.jobErrorMessage(responseJSON);
    });
  },

  onJobUpdateHandler: function () {
    app.clearCheckMessage();

    const payload = {
      jobName: $('#jobName').val(),
      jobGroup: $('#jobGroup').val(),
      className: $('#className').val(),
      classData: $('#classData').val(),
      classComment: $('#classComment').val(),
      classType: $('#classType').val(),
    };

    $.ajax({
      url: '/api/jobs',
      type: 'put',
      contentType: 'application/json; charset=utf-8;',
      dataType: 'json',
      data: JSON.stringify(payload)
    }).done(function ({ payload }) {
      alert('작업을 수정하였습니다.');
      window.location.reload();
    }).fail(function ({ responseJSON }) {
      schedule.jobErrorMessage(responseJSON);
    });
  },

  onJobPauseHandler: function (jobName, jobGroup) {
    const payload = {
      jobName,
      jobGroup,
      stateType: 'PAUSED',
    };

    const result = confirm('작업을 정지하시겠습니까?');

    if (result) {
      $.ajax({
        url: '/api/jobs/state',
        type: 'put',
        contentType: 'application/json; charset=utf-8;',
        dataType: 'json',
        data: JSON.stringify(payload)
      }).done(function () {
        alert('작업을 정지하였습니다.');
        window.location.reload();
      }).fail(function ({ responseJSON }) {
        schedule.jobStateErrorMessage(responseJSON);
      });
    }
  },

  onJobResumeHandler: function (jobName, jobGroup) {
    const payload = {
      jobName,
      jobGroup,
      stateType: 'RESUMED',
    };

    const result = confirm('작업을 재시작하시겠습니까?');

    if (result) {
      $.ajax({
        url: '/api/jobs/state',
        type: 'put',
        contentType: 'application/json; charset=utf-8;',
        dataType: 'json',
        data: JSON.stringify(payload)
      }).done(function () {
        alert('작업을 재시작하였습니다.');
        window.location.reload();
      }).fail(function ({ responseJSON }) {
        schedule.jobStateErrorMessage(responseJSON);
      });
    }
  },

  onJobDeleteHandler: function (jobName, jobGroup) {
    const payload = {
      jobName,
      jobGroup,
    };

    const result = confirm('작업을 삭제하시겠습니까?');

    if (result) {
      $.ajax({
        url: '/api/jobs',
        type: 'delete',
        contentType: 'application/json; charset=utf-8;',
        dataType: 'json',
        data: JSON.stringify(payload)
      }).done(function () {
        alert('작업을 삭제하였습니다.');
        window.location.reload();
      }).fail(function ({ responseJSON }) {
        schedule.jobStateErrorMessage(responseJSON);
      });
    }
  },

  onTriggerStoreHandler: function () {
    app.clearCheckMessage();

    const payload = {
      jobName: $('#jobName').val(),
      jobGroup: $('#jobGroup').val(),
      repeatCount: $('#repeatCount').val(),
      repeatInterval: $('#repeatInterval').val(),
      repeatExpression: $('#repeatExpression').val(),
      startTime: $('#startTime').val(),
      stopTime: $('#stopTime').val(),
      classType: $('#classType').val(),
      policyType: $('#policyType').val(),
    };

    $.ajax({
      url: '/api/triggers',
      type: 'post',
      contentType: 'application/json; charset=utf-8;',
      dataType: 'json',
      data: JSON.stringify(payload)
    }).done(function ({ payload }) {
      alert('트리거를 등록하였습니다.');
    }).fail(function ({ responseJSON }) {
      if (app.getCode(responseJSON) === 'SRC-003') {
        schedule.isMatchTriggerProps(payload);
      } else {
        alert('트리거 정보를 확인하세요.');
        app.setErrorFields(responseJSON);
      }
    });
  },

  onTriggerUpdateHandler: function () {
    app.clearCheckMessage();

    const payload = {
      jobName: $('#jobName').val(),
      jobGroup: $('#jobGroup').val(),
      triggerName: $('#triggerName').val(),
      triggerGroup: $('#triggerGroup').val(),
      repeatCount: $('#repeatCount').val(),
      repeatInterval: $('#repeatInterval').val(),
      repeatExpression: $('#repeatExpression').val(),
      startTime: $('#startTime').val(),
      stopTime: $('#stopTime').val(),
      classType: $('#classType').val(),
      policyType: $('#policyType').val(),
    };

    $.ajax({
      url: '/api/triggers',
      type: 'put',
      contentType: 'application/json; charset=utf-8;',
      dataType: 'json',
      data: JSON.stringify(payload)
    }).done(function ({ payload }) {
      alert('트리거를 수정하였습니다.');
      window.location.reload();
    }).fail(function ({ responseJSON }) {
      if (app.getCode(responseJSON) === 'SRC-003') {
        schedule.isMatchTriggerProps(payload);
      } else {
        alert('트리거 정보를 확인하세요.');
        app.setErrorFields(responseJSON);
      }
    });
  },

  onTriggerPauseHandler: function (jobName, jobGroup, triggerName, triggerGroup) {
    const payload = {
      jobName,
      jobGroup,
      triggerName,
      triggerGroup,
      stateType: 'PAUSED',
    }

    const result = confirm('트리거를 정지하시겠습니까?');

    if (result) {
      $.ajax({
        url: '/api/triggers/state',
        type: 'put',
        contentType: 'application/json; charset=utf-8;',
        dataType: 'json',
        data: JSON.stringify(payload)
      }).done(function () {
        alert('트리거를 정지하였습니다.');
        window.location.reload();
      }).fail(function ({ responseJSON }) {
        schedule.triggerStateErrorMessage(responseJSON);
      });
    }
  },

  onTriggerResumeHandler: function (jobName, jobGroup, triggerName, triggerGroup) {
    const payload = {
      jobName,
      jobGroup,
      triggerName,
      triggerGroup,
      stateType: 'RESUMED',
    }

    const result = confirm('트리거를 재시작하시겠습니까?');

    if (result) {
      $.ajax({
        url: '/api/triggers/state',
        type: 'put',
        contentType: 'application/json; charset=utf-8;',
        dataType: 'json',
        data: JSON.stringify(payload)
      }).done(function () {
        alert('트리거를 재시작하였습니다.');
        window.location.reload();
      }).fail(function ({ responseJSON }) {
        schedule.triggerStateErrorMessage(responseJSON);
      });
    }
  },

  onTriggerDeleteHandler: function (jobName, jobGroup, triggerName, triggerGroup) {
    const payload = {
      jobName,
      jobGroup,
      triggerName,
      triggerGroup,
    }

    const result = confirm('트리거를 삭제하시겠습니까?');

    if (result) {
      $.ajax({
        url: '/api/triggers',
        type: 'delete',
        contentType: 'application/json; charset=utf-8;',
        dataType: 'json',
        data: JSON.stringify(payload)
      }).done(function () {
        alert('트리거를 삭제하였습니다.');
        window.location.reload();
      }).fail(function ({ responseJSON }) {
        schedule.triggerStateErrorMessage(responseJSON);
      });
    }
  },

  selectClassType: function () {
    const $classType = $('#classType');
    const $className = $('#className');

    switch ($classType.val()) {
      case 'LOCAL':
        $className.val('system.routine.');
        break;
      case 'REMOTE':
        $className.val('system.remote.routine.');
        break;
    }
  },

  jobErrorMessage: function (responseJSON) {
    switch (app.getCode(responseJSON)) {
      case 'SRU-012': alert('작업 관리 권한이 없습니다.'); break;
      case 'SRJ-013': alert('작업 소유 권한이 없습니다.'); break;
      default: {
        alert('작업 정보를 확인하세요.');
        app.setErrorFields(responseJSON);
      }
    }
  },

  jobStateErrorMessage: function (responseJSON) {
    switch (app.getCode(responseJSON)) {
      case 'SRU-012': alert('작업 관리 권한이 없습니다.'); break;
      case 'SRJ-013': alert('작업 소유 권한이 없습니다.'); break;
      default: {
        alert('작업 상태를 변경할 수 없습니다.');
      }
    }
  },

  triggerErrorMessage: function (responseJSON) {
    switch (app.getCode(responseJSON)) {
      case 'SRU-012': alert('트리거 관리 권한이 없습니다.'); break;
      case 'SRT-011': alert('트리거 소유 권한이 없습니다.'); break;
      default: {
        alert('트리거 정보를 확인하세요.');
        app.setErrorFields(responseJSON);
      }
    }
  },

  triggerStateErrorMessage: function (responseJSON) {
    switch (app.getCode(responseJSON)) {
      case 'SRU-012': alert('트리거 관리 권한이 없습니다.'); break;
      case 'SRT-011': alert('트리거 소유 권한이 없습니다.'); break;
      default: {
        alert('트리거 상태를 변경할 수 없습니다.');
      }
    }
  },

  isMatchTriggerProps: function (payload) {
    if (!validate.validateDateTime(payload.startTime)) {
      app.setErrorField($(`#startTime`), '시작 시간을 확인하세요.');
    }
    if (!validate.validateDateTime(payload.stopTime)) {
      app.setErrorField($(`#stopTime`), '종료 시간을 확인하세요.');
    }
  },
};

schedule.init();