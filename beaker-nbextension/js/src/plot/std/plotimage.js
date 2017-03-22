/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

define([
  './../plotUtils'
], function(
  plotUtils
) {

  var PlotImage = function(data){
    _.extend(this, data); // copy properties to itself
    this.format();
  };

  PlotImage.prototype.plotClass = "plot-image";

  PlotImage.prototype.format = function(){
    this.itemProps = {
      "id" : this.id,
      "st" : this.color,
      "st_op": this.color_opacity,
      "st_w" : this.width,
      // not sure how this works for now
      "st_h" : this.height,
      "st_f" : this.format,
      "st_v" : this.value
    };

    this.elementProps = [];
    this.labelpipe = [];
    this.rmlabelpipe = [];
  };

  PlotImage.prototype.render = function(scope){
    if (this.showItem === false) {
      this.clear(scope);
      return;
    }
    this.filter(scope);
    console.log("TZ prepare pics");
    this.prepare(scope);
    if (this.vlength === 0) {
      this.clear(scope);
    } else {
      console.log("TZ draw pics");
      this.draw(scope);
    }
  };

  PlotImage.prototype.getRange = function() {
    var eles = this.elements;
    var range = {
      xl : Infinity,
      xr : -Infinity,
      yl : Infinity,
      yr : -Infinity,
    };
    for (var i = 0; i < eles.length; i++) {
      var ele = eles[i];
      if (ele.type === "x") {
        range.xl = Math.min(range.xl, ele.x);
        range.xr = Math.max(range.xr, ele.x);
      } else if (ele.type === "y") {
        range.yl = Math.min(range.yl, ele.y);
        range.yr = Math.max(range.yr, ele.y);
      }
    }
    return range;
  };

  PlotImage.prototype.applyAxis = function(xAxis, yAxis) {
    this.xAxis = xAxis;
    this.yAxis = yAxis;
    for (var i = 0; i < this.elements.length; i++) {
      var ele = this.elements[i];
      if (ele.type === "x") {
        ele.x = xAxis.getPercent(ele.x);
      } else if (ele.type === "y") {
        ele.y = yAxis.getPercent(ele.y);
      }
    }
  };

  PlotImage.prototype.filter = function(scope) {
    // do nothing and show everything
    var l = 0, r = this.elements.length - 1;
    this.vindexL = l;
    this.vindexR = r;
    this.vlength = r - l + 1;
  };

  PlotImage.prototype.useSecondYAxis = function(scope) {
    var axisLabelExist = this.yAxisLabel !== undefined && this.yAxisLabel !== null;
    return axisLabelExist && scope.data2scrYi_r;
  };

  PlotImage.prototype.getYMapper = function(scope) {
    return this.useSecondYAxis(scope) ? scope.data2scrYi_r : scope.data2scrYi;
  };


  PlotImage.prototype.prepare = function(scope) {
    var focus = scope.focus;
    var eles = this.elements,
      eleprops = this.elementProps;
    var mapX = scope.data2scrXi,
      mapY = this.getYMapper(scope);
    var lMargin = scope.layout.leftLayoutMargin,
      bMargin = scope.layout.bottomLayoutMargin;

    eleprops.length = 0;
    this.labelpipe.length = 0;
    this.rmlabelpipe.length = 0;

    for (var i = this.vindexL; i <= this.vindexR; i++) {
      var ele = eles[i];

      var prop = {
        "id" : this.id + "_" + i,
        "lbid" : this.id + "_" + i + "l",
        "x": ele.x,
        "y": ele.y,
        "st" : ele.color,
        "st_op" : ele.color_opacity,
        "st_w" : ele.width,
        "bg_clr" : ele.color == null ? this.color : ele.color,
        "st_h" : ele.height,
        "st_f" : ele.format,
        "st_v" : ele.value
      };
      eleprops.push(prop);
    }
  };


  PlotImage.prototype.draw = function(scope) {
    var svg = scope.maing;
    var props = this.itemProps,
      eleprops = this.elementProps;
    
    console.log("props", props);
    console.log("eleprops", eleprops);

    if (svg.select("#" + this.id).empty()) {
      svg.selectAll("g")
        .data([props], function(d){ return d.id; }).enter().append("g")
        .attr("id", function(d) { return d.id; });
    }
    svg.select("#" + this.id)
      .attr("class", this.plotClass);

    var svgitem = svg.select("#" + this.id);
    svgitem.selectAll("image")
      .data(eleprops, function(d) { return d.id; }).exit().remove();
    
    svgitem.selectAll("image")
      .data(eleprops, function(d) { return d.id; }).enter().append("image")
      .attr("id", function(d) { return d.id; })
      .attr("x", function(d) { return d.x; })
      .attr("y", function(d) { return d.y; })
      .attr("width", function(d) { return d.st_w; })
      .attr("height", function(d) { return d.st_h; })
      .attr("xlink:href", function(d) { 
        console.log("data", d.st_v);
        return d.st_v; 
      });
      //.attr("class", this.respClass) // does not need resp

  };

  PlotImage.prototype.clear = function(scope) {
    scope.maing.select("#" + this.id).selectAll("*").remove();
  };

  PlotImage.prototype.hideTips = function(scope, hidden) {
    // do nothing, no tip for this type
  };

  return PlotImage;

});