var exec = require('cordova/exec');
module.exports = {
    getAdInfo: function (success, error) {
        exec(success, error, "AdvertisingIDFA", "getAdInfo", []);
    },
    getAdId: function (success, error) {
        exec(success, error, "AdvertisingIDFA", "getAdId", []);
    },
    isTrackingEnabled: function (success, error) {
        exec(success, error, "AdvertisingIDFA", "isTrackingEnabled", []);
    }
};