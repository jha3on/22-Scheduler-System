let secure = {
  init: function () {
    let _this = this;
    console.log('secure.init()');
  },

  isMatchUserCode: function (userCode) {
    const regex = /^\d{6}$/;

    return regex.test(userCode);
  },

  isMatchUserName: function (userName) {
    const regex = /^[가-힣]{1,5}$/;

    return regex.test(userName);
  },

  isMatchUserEmail: function (userEmail) {
    const regex = /^\S+@\S+\.\S+$/;

    return regex.test(userEmail);
  },

  isMatchUserPassword: function (userPassword) {
    const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{1,10}$/;

    return regex.test(userPassword);
  },

  checkSignUserCode: function () {
    const $userCode = $('#userCode');
    const userCode = $userCode.val();

    if (secure.isMatchUserCode(userCode)) {
      $.ajax({
        url: '/api/users/code',
        type: 'get',
        contentType: 'application/json; charset=utf-8;',
        dataType: 'json',
        data: { userCode }
      }).done(function ({ payload }) {
        (payload === false)
          ? app.setCheckMessage($userCode, '등록할 수 있는 사번입니다.')
          : app.setCheckMessage($userCode, '이미 등록된 사번입니다.', false);
      }).fail(function () {
        app.alertErrorMessage();
      });
    } else {
      app.setCheckMessage($userCode, '사번 형식(숫자 6자)을 확인하세요.', false);
    }
  },

  checkSignUserName: function () {
    const $userName = $('#userName');
    const userName = $userName.val();

    secure.isMatchUserName(userName)
      ? app.setCheckMessage($userName, '등록할 수 있는 이름입니다.')
      : app.setCheckMessage($userName, '이름 형식(국문 5자 이내)을 확인하세요.', false);
  },

  checkSignUserEmail: function () {
    const $userEmail = $('#userEmail');
    const userEmail = $userEmail.val();

    if (secure.isMatchUserEmail(userEmail)) {
      $.ajax({
        url: '/api/users/email',
        type: 'get',
        contentType: 'application/json; charset=utf-8;',
        dataType: 'json',
        data: { userEmail }
      }).done(function ({ payload }) {
        (payload === false)
          ? app.setCheckMessage($userEmail, '등록할 수 있는 이메일입니다.')
          : app.setCheckMessage($userEmail, '이미 등록된 이메일입니다.', false);
      }).fail(function () {
        app.alertErrorMessage();
      });
    } else {
      app.setCheckMessage($userEmail, '이메일 형식을 확인하세요.', false);
    }
  },

  checkSignUserPassword: function () {
    const $userPassword = $('#userPassword');
    const userPassword = $userPassword.val();

    secure.isMatchUserPassword(userPassword)
      ? app.setCheckMessage($userPassword, '등록할 수 있는 비밀번호입니다.')
      : app.setCheckMessage($userPassword, '비밀번호 형식(영문, 숫자 10자 이내)을 확인하세요.', false);
  },

  onSignHandler: function () {
    const userCode = $('#userCode').val();
    const userName = $('#userName').val();
    const userEmail = $('#userEmail').val();
    const userPassword = $('#userPassword').val();

    $.ajax({
      url: '/api/users/sign',
      type: 'post',
      contentType: 'application/json; charset=utf-8;',
      dataType: 'json',
      data: JSON.stringify({ userCode, userName, userEmail, userPassword })
    }).done(function () {
      alert('사용자 정보를 등록하였습니다.');
      window.location.replace('/user/login');
    }).fail(function ({ responseJSON }) {
      alert('사용자 정보를 확인하세요.');
      app.setErrorFields(responseJSON);
    });
  },

  onLoginHandler: function () {
    const userEmail = $('#userEmail').val();
    const userPassword = $('#userPassword').val();

    $.ajax({
      url: '/api/users/login',
      type: 'post',
      contentType: 'application/json; charset=utf-8;',
      dataType: 'json',
      data: JSON.stringify({ userEmail, userPassword })
    }).done(function ({ payload }) {
      window.location.replace(payload);
    }).fail(function ({ responseJSON }) {
      alert(app.getMessage(responseJSON));
    });
  },

  onLogoutHandler: function () {
    const result = confirm('로그아웃하시겠습니까?');

    if (result) {
      $.ajax({
        url: '/api/users/logout',
        type: 'post',
        contentType: 'application/json; charset=utf-8;',
        dataType: 'json'
      }).done(function () {
        window.location.replace('/user/login');
      }).fail(function ({ responseJSON }) {
        alert(app.getMessage(responseJSON));
      });
    }
  }
};

secure.init();