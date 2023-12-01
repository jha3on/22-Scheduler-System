let app = {
  init: function () {
    let _this = this;
    console.log('app.init()');
  },

  refresh: function () {
    window.location.reload();
  },

  backward: function () {
    window.history.back();
  },

  getCode: function (result) {
    return result.code;
  },

  getMessage: function (result) {
    return result.message;
  },

  getPayload: function (result) {
    return result.payload;
  },

  alertErrorMessage: function () {
    alert('서버 오류가 발생하였습니다.');
  },

  setCheckMessage: function ($target, message, isValid = true) {
    const color = isValid ? '#0362FF' : '#F1416C';
    const $check = $target.siblings('.check'); // span 태그 조회

    // !$check.empty()
    if ($check.length > 0) {
      $check.text(message).css('color', color);
    } else {
      $target.after(`<span class="check">${message}</span>`);
      $target.siblings('.check').css('color', color);
    }
  },

  setErrorField: function ($target, message) {
    $target.siblings('.check').remove(); // span 태그 삭제
    $target.after(`<span class="check">${message}</span>`);
    $target.siblings('.check').css('color', '#F1416C');
  },

  setErrorFields: function (result) {
    const errorFields = app.getPayload(result).errors;

    if (!errorFields) {
      alert(result.message);
      return;
    }

    for (let i = 0, length = errorFields.length; i < length; i++) {
      const error = errorFields[i]; // console.log(error);
      const $target = $(`#${error['field']}`); // #userCode, #userName, ... (-> input.id)
      
      if ($target && $target.length > 0) {
        $target.siblings('.check').remove(); // span 태그 삭제
        $target.after(`<span class="check">${error.message}</span>`);
        $target.siblings('.check').css('color', '#F1416C');
      }
    }
  },

  formatDateTime: function (input = new HTMLInputElement()) {
    let value = input.value.replace(/\D/g, "");
    value = value.replace(/^(\d{4})(\d+)$/g, "$1-$2");
    value = value.replace(/^(\d{4}-\d{2})(\d+)$/g, "$1-$2");
    value = value.replace(/^(\d{4}-\d{2}-\d{2})(\d+)$/g, "$1 $2");
    value = value.replace(/^(\d{4}-\d{2}-\d{2} \d{2})(\d+)$/g, "$1:$2");
    value = value.replace(/^(\d{4}-\d{2}-\d{2} \d{2}:\d{2})(\d+)$/g, "$1:$2");
    input.value = value;
    input.maxLength = 19;

    return value;
  },

  clearForm: function () {
    $('.check').remove(); // span 태그 삭제
    $('form')[0].reset();
  },

  clearCheckMessage: function () {
    $('.check').remove(); // span 태그 삭제
  },

  removeCheckMessage: function (element) {
    $(element).siblings('.check').remove(); // span 태그 삭제
  },
};

app.init();