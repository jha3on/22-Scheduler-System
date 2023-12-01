let validate = {
  init: function () {
    let _this = this;
    console.log('validate.init()');
  },

  validateEmpty: function (value) {
    return value === "" || value === null || value === undefined || (typeof value === "object" && !Object.keys(value).length);
  },

  validateNumber: function (value) {
    return isNaN(value);
  },

  validateJson: function (value) {
    try {
      const json = JSON.parse(value);
      return (typeof json === 'object');
    } catch (e) {
      return false;
    }
  },

  validateDateTime: function (value) {
    const regex = /\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\d|3[0-1]) (2[0-3]|[01]\d):[0-5]\d/;

    return regex.test(value);
  }
}

validate.init();