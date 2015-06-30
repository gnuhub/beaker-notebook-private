var _ = require('lodash');
var config  = require('./_config');
var Promise = require('bluebird');
var req = require('request');
var post = Promise.promisify(req.post);
var put = Promise.promisify(req.put);
var del = Promise.promisify(req.del);
var get = Promise.promisify(req.get);
var util = require('util');
var uuid = require('node-uuid');

module.exports = function() {

  var currentDatasetId = 1;

  function ensureSuccess(response, indexName) {
    if (response[0].statusCode != 201) {
      throw new Error(util.format(
        'Marketplace error: \r\nhttpCode: %s\nresponse: %s',
        response[0].statusCode, response[1]));
    }
    return refresh(indexName);
  }

  function refresh(indexName) {
    var payload = JSON.stringify({indexName: indexName});
    return put({
      url: config.marketplaceUrl + '/refresh',
      body: payload,
      headers: {
        'content-type': 'application/json'
      }
    });
  }

  function createRecords(indexName, recordType, records) {
    var payloadObj = {indexName: indexName};
    payloadObj[recordType] = Array.prototype.concat(records);
    recordType = recordType.indexOf('datasets') > -1 ? 'seed/' + recordType : recordType;
    var payload = JSON.stringify(payloadObj);
    return post({
      url: config.marketplaceUrl + '/' + recordType,
      body: payload,
      headers: {
        'content-type': 'application/json'
      }
    })
    .then(function(response) {
      return ensureSuccess(response, indexName);
    });
  }

  function getVendorByName(name) {
    return get(config.marketplaceUrl + '/vendors')
    .then(function(res) {
      var vendors = JSON.parse(res[0].body);
      return _.find(vendors, function(v) {
        return v.name === name;
      });
    });
  }

  this.marketplace = {
    createVendors: function(vendors) {
      return Promise.each(vendors, function(vendor) {
        _.extend(vendor, {'public-id': uuid.v1()});
        return post({
          url: config.marketplaceUrl + '/vendors',
          body: JSON.stringify(vendor),
          headers: {
            'Content-Type': 'application/json'
          }
        });
      });
    },

    createIndex: function(indexName) {
      var payload = JSON.stringify({indexName: indexName});
      return post({
        url: config.marketplaceUrl + '/indices',
        body: payload,
        headers: {
          'content-type': 'application/json'
        }
      });
    },

    createCatalog: function(attrs) {
      var payload = JSON.stringify(attrs);
      return post({url: config.marketplaceUrl + '/catalogs',
                   body: payload,
                   headers: {'Content-type': 'application/json'}})
      .then(function(response) {
        return JSON.parse(response[0].body);
      });
    },

    createCategory: function(attrs) {
      var payload = JSON.stringify(attrs);
      return post({url: config.marketplaceUrl + '/categories',
                   body: payload,
                   headers: {'Content-type': 'application/json'}})
      .then(function(response) {
        return JSON.parse(response[0].body);
      });
    },

    createDatasets: function(indexName, datasets) {
      return Promise.map(datasets, function(set) {
        return getVendorByName(set.vendor)
        .then(function(vendor) {
          set.vendor = vendor['public-id'];
          set.id = set.id || currentDatasetId;
          currentDatasetId += 1;
          return set;
        });
      }).then(function(datasets) {
        return createRecords(indexName, 'datasets', datasets);
      });
    },

    deleteSeed: function() {
      return del(config.marketplaceUrl + '/seed');
    }
  };

  return this.marketplace;
};
