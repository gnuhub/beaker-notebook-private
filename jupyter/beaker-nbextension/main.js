/// / Beaker Autotranslation Notebook Extension
define([
  'services/config',
  'services/kernels/comm',
  'base/js/utils',
  'base/js/namespace',
  'base/js/events',
  'require'
], function(
  configmod,
  comm,
  utils,
  Jupyter,
  events,
  require
) {
  "use strict";

  var base_url = utils.get_body_data('baseUrl');
  var config = new configmod.ConfigSection('notebook', {base_url: base_url});
  var comm;

  config.loaded.then(function() {
    console.log('beaker extension loaded');
  });

  Jupyter.notebook.events.on('kernel_ready.Kernel', function() {
    var kernel = Jupyter.notebook.kernel;
    window.beaker = {};
    kernel.comm_manager.register_target('beaker.autotranslation',
      function(comm, msg) {
        comm.on_msg(function(msg) {
          window.beaker[msg.content.data.name] = JSON.parse(msg.content.data.value);
        });
      });
  });

  var load_ipython_extension = function() {
    load_css('bower_components/datatables/media/css/jquery.dataTables.min.css');
    load_css('bower_components/datatables.net-colreorder-dt/css/colReorder.dataTables.min.css');
    load_css('bower_components/datatables.net-fixedcolumns-dt/css/fixedColumns.dataTables.min.css');
    load_css('bower_components/datatables.net-keytable-dt/css/keyTable.dataTables.min.css');
    load_css('bower_components/jQuery-contextMenu/dist/jquery.contextMenu.min.css');
    load_css('plot/bko-combinedplot.css');
    load_css('plot/bko-plot.css');
    load_css('tableDisplay/css/datatables.css');
    start();
  };

  function load_css(name) {
    var link = document.createElement("link");
    link.type = "text/css";
    link.rel = "stylesheet";
    link.href = require.toUrl("nbextensions/beaker/"+name);
    document.getElementsByTagName("head")[0].appendChild(link);
  }

  function start() {
    require(['nbextensions/beaker/reqConfig', 'require'], function(conf, require) {
      require(['nbextensions/beaker/beakerManager'], function(beakerManager) {
        window.initPlotd = function(data, wrapperId) {
          console.log('init plotd');
          beakerManager.init(data, wrapperId);
        };
      });
    });

    config.load();


    //events.on('kernel_connected.Kernel',function(kernel){console.log('dddddddddddddddddddddd');});


    events.on('kernel_connected.Kernel',function(kernel){

      console.log('kernel_connected = ' + kernel);

      var kernel_control_data = {};
      kernel_control_data.data = {};
      kernel_control_data.data.imports = "abc";

      var kernel_control_target_name = "kernel.control.chanel";
      var comm = null;

      for(var property in Jupyter.notebook.kernel.comm_manager.comms){
        if(object.hasOwnProperty(property)){
          Jupyter.notebook.kernel.comm_manager.comms['"' + property +'"'].then(function(s){
            if(kernel_control_target_name === s.target_name){
              comm = s;
            }
          })
        }
      }

      if(comm == null){
        console.log('kernel.control.chanel comm open');
        Jupyter.notebook.kernel.comm_manager.new_comm(kernel_control_target_name,kernel_control_data,null,null,utils.uuid());
      }else{
       console.log('kernel.control.chanel comm send');
      //  comm.then(function(o){o.send(kernel_control_target_name)});
        //Jupyter.notebook.kernel.comm_manager.comms["eee"].then(function(o){o.send("hello")});
      }

   });



  }

  return {
    load_ipython_extension : load_ipython_extension
  };
});
