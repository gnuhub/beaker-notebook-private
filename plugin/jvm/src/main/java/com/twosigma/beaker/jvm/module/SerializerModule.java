/*
 *  Copyright 2014 TWO SIGMA OPEN SOURCE, LLC
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
package com.twosigma.beaker.jvm.module;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.twosigma.beaker.BeakerCodeCell;
import com.twosigma.beaker.BeakerProgressUpdate;
import com.twosigma.beaker.NamespaceClient;
import com.twosigma.beaker.jvm.object.OutputCell;
import com.twosigma.beaker.chart.Color;
import com.twosigma.beaker.chart.GradientColor;
import com.twosigma.beaker.chart.categoryplot.CategoryPlot;
import com.twosigma.beaker.chart.categoryplot.plotitem.CategoryBars;
import com.twosigma.beaker.chart.categoryplot.plotitem.CategoryLines;
import com.twosigma.beaker.chart.categoryplot.plotitem.CategoryPoints;
import com.twosigma.beaker.chart.categoryplot.plotitem.CategoryStems;
import com.twosigma.beaker.chart.heatmap.HeatMap;
import com.twosigma.beaker.chart.histogram.Histogram;
import com.twosigma.beaker.chart.legend.LegendPosition;
import com.twosigma.beaker.chart.serializer.AreaSerializer;
import com.twosigma.beaker.chart.serializer.BarsSerializer;
import com.twosigma.beaker.chart.serializer.CategoryBarsSerializer;
import com.twosigma.beaker.chart.serializer.CategoryLinesSerializer;
import com.twosigma.beaker.chart.serializer.CategoryPlotSerializer;
import com.twosigma.beaker.chart.serializer.CategoryPointsSerializer;
import com.twosigma.beaker.chart.serializer.CategoryStemsSerializer;
import com.twosigma.beaker.chart.serializer.ColorSerializer;
import com.twosigma.beaker.chart.serializer.CombinedPlotSerializer;
import com.twosigma.beaker.chart.serializer.ConstantLineSerializer;
import com.twosigma.beaker.chart.serializer.ConstantBandSerializer;
import com.twosigma.beaker.chart.serializer.CrosshairSerializer;
import com.twosigma.beaker.chart.serializer.GradientColorSerializer;
import com.twosigma.beaker.chart.serializer.HeatMapSerializer;
import com.twosigma.beaker.chart.serializer.HistogramSerializer;
import com.twosigma.beaker.chart.serializer.LegendPositionSerializer;
import com.twosigma.beaker.chart.serializer.LineSerializer;
import com.twosigma.beaker.chart.serializer.PointsSerializer;
import com.twosigma.beaker.chart.serializer.StemsSerializer;
import com.twosigma.beaker.chart.serializer.TextSerializer;
import com.twosigma.beaker.chart.serializer.TreeMapNodeSerializer;
import com.twosigma.beaker.chart.serializer.TreeMapSerializer;
import com.twosigma.beaker.chart.serializer.XYChartSerializer;
import com.twosigma.beaker.chart.serializer.YAxisSerializer;
import com.twosigma.beaker.chart.treemap.TreeMap;
import com.twosigma.beaker.chart.xychart.CombinedPlot;
import com.twosigma.beaker.chart.xychart.XYChart;
import com.twosigma.beaker.chart.xychart.plotitem.Area;
import com.twosigma.beaker.chart.xychart.plotitem.Bars;
import com.twosigma.beaker.chart.xychart.plotitem.ConstantLine;
import com.twosigma.beaker.chart.xychart.plotitem.ConstantBand;
import com.twosigma.beaker.chart.xychart.plotitem.Crosshair;
import com.twosigma.beaker.chart.xychart.plotitem.Line;
import com.twosigma.beaker.chart.xychart.plotitem.Points;
import com.twosigma.beaker.chart.xychart.plotitem.Stems;
import com.twosigma.beaker.chart.xychart.plotitem.Text;
import com.twosigma.beaker.chart.xychart.plotitem.YAxis;
import com.twosigma.beaker.easyform.EasyForm;
import com.twosigma.beaker.easyform.EasyFormObjectManager;
import com.twosigma.beaker.easyform.formitem.ButtonComponent;
import com.twosigma.beaker.easyform.formitem.CheckBox;
import com.twosigma.beaker.easyform.formitem.CheckBoxGroup;
import com.twosigma.beaker.easyform.formitem.ComboBox;
import com.twosigma.beaker.easyform.formitem.DatePickerComponent;
import com.twosigma.beaker.easyform.formitem.ListComponent;
import com.twosigma.beaker.easyform.formitem.LoadValuesButton;
import com.twosigma.beaker.easyform.formitem.RadioButtonComponent;
import com.twosigma.beaker.easyform.formitem.SaveValuesButton;
import com.twosigma.beaker.easyform.formitem.TextArea;
import com.twosigma.beaker.easyform.formitem.TextField;
import com.twosigma.beaker.easyform.serializer.ButtonComponentSerializer;
import com.twosigma.beaker.easyform.serializer.CheckBoxGroupSerializer;
import com.twosigma.beaker.easyform.serializer.CheckBoxSerializer;
import com.twosigma.beaker.easyform.serializer.ComboBoxSerializer;
import com.twosigma.beaker.easyform.serializer.DatePickerComponentSerializer;
import com.twosigma.beaker.easyform.serializer.EasyFormSerializer;
import com.twosigma.beaker.easyform.serializer.ListComponentSerializer;
import com.twosigma.beaker.easyform.serializer.LoadValuesButtonSerializer;
import com.twosigma.beaker.easyform.serializer.RadioButtonSerializer;
import com.twosigma.beaker.easyform.serializer.SaveValuesButtonSerializer;
import com.twosigma.beaker.easyform.serializer.TextAreaSerializer;
import com.twosigma.beaker.easyform.serializer.TextFieldSerializer;
import com.twosigma.beaker.jvm.object.BeakerDashboard;
import com.twosigma.beaker.jvm.object.CyclingOutputContainerLayoutManager;
import com.twosigma.beaker.jvm.object.EvaluationResult;
import com.twosigma.beaker.jvm.object.GridOutputContainerLayoutManager;
import com.twosigma.beaker.jvm.object.OutputContainer;
import com.twosigma.beaker.jvm.object.OutputContainerCell;
import com.twosigma.beaker.jvm.object.SimpleEvaluationObject;
import com.twosigma.beaker.jvm.object.SimpleLayoutManager;
import com.twosigma.beaker.jvm.object.TabbedOutputContainerLayoutManager;
import com.twosigma.beaker.table.TableDisplay;
import com.twosigma.beaker.jvm.object.UpdatableEvaluationResult;
import com.twosigma.beaker.jvm.object.DashboardLayoutManager;
import com.twosigma.beaker.jvm.serialization.BeakerCodeCellList;
import com.twosigma.beaker.jvm.serialization.BeakerCodeCellListDeserializer;
import com.twosigma.beaker.jvm.serialization.BeakerObjectConverter;
import com.twosigma.beaker.jvm.serialization.BufferedImageDeserializer;
import com.twosigma.beaker.jvm.serialization.BufferedImageSerializer;
import com.twosigma.beaker.jvm.serialization.CollectionDeserializer;
import com.twosigma.beaker.jvm.serialization.CyclingOutputContainerLayoutManagerDeserializer;
import com.twosigma.beaker.jvm.serialization.CyclingOutputContainerLayoutManagerSerializer;
import com.twosigma.beaker.jvm.serialization.DashboardLayoutManagerDeserializer;
import com.twosigma.beaker.jvm.serialization.ColorDeserializer;
import com.twosigma.beaker.jvm.serialization.DateDeserializer;
import com.twosigma.beaker.jvm.serialization.DateSerializer;
import com.twosigma.beaker.jvm.serialization.GridOutputContainerLayoutManagerDeserializer;
import com.twosigma.beaker.jvm.serialization.GridOutputContainerLayoutManagerSerializer;
import com.twosigma.beaker.jvm.serialization.ImageIconSerializer;
import com.twosigma.beaker.jvm.serialization.MapDeserializer;
import com.twosigma.beaker.jvm.serialization.NamespaceBindingDeserializer;
import com.twosigma.beaker.jvm.serialization.OutputCellStateSerializer;
import com.twosigma.beaker.jvm.serialization.OutputContainerCellDeserializer;
import com.twosigma.beaker.jvm.serialization.OutputContainerCellSerializer;
import com.twosigma.beaker.jvm.serialization.OutputContainerDeserializer;
import com.twosigma.beaker.jvm.serialization.OutputContainerSerializer;
import com.twosigma.beaker.jvm.serialization.PlotObjectSerializer;
import com.twosigma.beaker.jvm.serialization.ResultsDeserializer;
import com.twosigma.beaker.jvm.serialization.SimpleLayoutManagerDeserializer;
import com.twosigma.beaker.jvm.serialization.SimpleLayoutManagerSerializer;
import com.twosigma.beaker.jvm.serialization.TabbedOutputContainerLayoutManagerDeserializer;
import com.twosigma.beaker.jvm.serialization.TabbedOutputContainerLayoutManagerSerializer;
import com.twosigma.beaker.jvm.serialization.DashboardLayoutManagerSerializer;
import com.twosigma.beaker.shared.NamespaceBinding;
import com.twosigma.beaker.shared.json.serializer.StringObject;
import com.twosigma.beaker.table.TableDisplayAlignmentProvider;
import com.twosigma.beaker.table.format.DecimalStringFormat;
import com.twosigma.beaker.table.format.TimeStringFormat;
import com.twosigma.beaker.table.format.ValueStringFormat;
import com.twosigma.beaker.table.highlight.HeatmapHighlighter;
import com.twosigma.beaker.table.highlight.ThreeColorHeatmapHighlighter;
import com.twosigma.beaker.table.highlight.UniqueEntriesHighlighter;
import com.twosigma.beaker.table.highlight.ValueHighlighter;
import com.twosigma.beaker.table.renderer.DataBarsRenderer;
import com.twosigma.beaker.table.serializer.DataBarsRendererSerializer;
import com.twosigma.beaker.table.serializer.DecimalStringFormatSerializer;
import com.twosigma.beaker.table.serializer.HeatmapHighlighterSerializer;
import com.twosigma.beaker.table.serializer.TableDisplayAlignmentSerializer;
import com.twosigma.beaker.table.serializer.TableDisplayDeSerializer;
import com.twosigma.beaker.table.serializer.TableDisplaySerializer;
import com.twosigma.beaker.table.serializer.ThreeColorHeatmapHighlighterSerializer;
import com.twosigma.beaker.table.serializer.TimeStringFormatSerializer;
import com.twosigma.beaker.table.serializer.UniqueEntriesHighlighterSerializer;
import com.twosigma.beaker.table.serializer.ValueHighlighterSerializer;
import com.twosigma.beaker.table.serializer.ValueStringFormatSerializer;
import net.sf.jtreemap.swing.TreeMapNode;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * The Guice module as the registry of mapping from classes to serializers
 */
public class SerializerModule extends AbstractModule {
  private final static Logger logger = LoggerFactory.getLogger(SerializerModule.class.getName());
  
  @Override
  protected void configure() {
    bind(SimpleEvaluationObject.Serializer.class);
    bind(EvaluationResult.Serializer.class);
    bind(UpdatableEvaluationResult.Serializer.class);
    bind(OutputContainerSerializer.class);
    bind(BeakerProgressUpdate.Serializer.class);
    bind(EasyFormObjectManager.class);
    bind(EasyFormSerializer.class);
  }

  @Provides
  @Singleton
  public BeakerObjectConverter getObjectSerializer(Injector injector) {
    BeakerObjectConverter serializer = injector.getInstance(PlotObjectSerializer.class);
    try {
      serializer.addTypeDeserializer(new TableDisplayDeSerializer(serializer));
      serializer.addTypeDeserializer(new BeakerCodeCell.DeSerializer(serializer));
      serializer.addTypeDeserializer(new BeakerProgressUpdate.DeSerializer(serializer));
      serializer.addTypeDeserializer(new SimpleEvaluationObject.DeSerializer(serializer));
      serializer.addTypeDeserializer(new UpdatableEvaluationResult.DeSerializer(serializer)); 
      serializer.addTypeDeserializer(new BufferedImageDeserializer(serializer));
      serializer.addTypeDeserializer(new DateDeserializer(serializer));
      serializer.addTypeDeserializer(new ResultsDeserializer(serializer));
      serializer.addTypeDeserializer(new CollectionDeserializer(serializer));
      serializer.addTypeDeserializer(new MapDeserializer(serializer));
      serializer.addTypeDeserializer(new ColorDeserializer(serializer));

      serializer.addTypeDeserializer(new OutputContainerDeserializer(serializer));
      serializer.addTypeDeserializer(new OutputContainerCellDeserializer(serializer));
      serializer.addTypeDeserializer(new SimpleLayoutManagerDeserializer(serializer));
      serializer.addTypeDeserializer(new TabbedOutputContainerLayoutManagerDeserializer(serializer));
      serializer.addTypeDeserializer(new CyclingOutputContainerLayoutManagerDeserializer(serializer));
      serializer.addTypeDeserializer(new GridOutputContainerLayoutManagerDeserializer(serializer));
      serializer.addTypeDeserializer(new DashboardLayoutManagerDeserializer(serializer));

      } catch(Exception e) {
      logger.error("exception while creating ObjectSerializer", e);
    }
    return serializer;
  }

  @Provides
  @Singleton
  public ObjectMapper getObjectMapper(Injector injector) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      SimpleModule module =
          new SimpleModule("MySerializer", new Version(1, 0, 0, null));

      // this is the root object we deserialize when reading from notebook namespace
      module.addDeserializer(NamespaceBinding.class, injector.getInstance(NamespaceBindingDeserializer.class));
      module.addDeserializer(BeakerCodeCellList.class, injector.getInstance(BeakerCodeCellListDeserializer.class));
      
      // all these objects can be returned as values and/or stored in the notebook namespace
      module.addSerializer(SimpleEvaluationObject.class, injector.getInstance(SimpleEvaluationObject.Serializer.class));
      module.addSerializer(EvaluationResult.class, injector.getInstance(EvaluationResult.Serializer.class));
      module.addSerializer(UpdatableEvaluationResult.class, injector.getInstance(UpdatableEvaluationResult.Serializer.class));
      module.addSerializer(BeakerProgressUpdate.class, injector.getInstance(BeakerProgressUpdate.Serializer.class));
      module.addSerializer(BeakerCodeCell.class, injector.getInstance(BeakerCodeCell.Serializer.class));

      module.addSerializer(TableDisplay.class, injector.getInstance(TableDisplaySerializer.class));
      module.addSerializer(DecimalStringFormat.class, injector.getInstance(DecimalStringFormatSerializer.class));
      module.addSerializer(TimeStringFormat.class, injector.getInstance(TimeStringFormatSerializer.class));
      module.addSerializer(ValueStringFormat.class, injector.getInstance(ValueStringFormatSerializer.class));
      module.addSerializer(DataBarsRenderer.class, injector.getInstance(DataBarsRendererSerializer.class));
      module.addSerializer(TableDisplayAlignmentProvider.class, injector.getInstance(TableDisplayAlignmentSerializer.class));
      module.addSerializer(HeatmapHighlighter.class, injector.getInstance(HeatmapHighlighterSerializer.class));
      module.addSerializer(ThreeColorHeatmapHighlighter.class, injector.getInstance(ThreeColorHeatmapHighlighterSerializer.class));
      module.addSerializer(UniqueEntriesHighlighter.class, injector.getInstance(UniqueEntriesHighlighterSerializer.class));
      module.addSerializer(ValueHighlighter.class, injector.getInstance(ValueHighlighterSerializer.class));

      module.addSerializer(OutputContainer.class, injector.getInstance(OutputContainerSerializer.class));
      module.addSerializer(OutputContainerCell.class, injector.getInstance(OutputContainerCellSerializer.class));
      module.addSerializer(OutputCell.State.class, injector.getInstance(OutputCellStateSerializer.class));
      module.addSerializer(SimpleLayoutManager.class, injector.getInstance(SimpleLayoutManagerSerializer.class));
      module.addSerializer(TabbedOutputContainerLayoutManager.class, injector.getInstance(TabbedOutputContainerLayoutManagerSerializer.class));
      module.addSerializer(GridOutputContainerLayoutManager.class, injector.getInstance(GridOutputContainerLayoutManagerSerializer.class));
      module.addSerializer(CyclingOutputContainerLayoutManager.class, injector.getInstance(CyclingOutputContainerLayoutManagerSerializer.class));
      module.addSerializer(DashboardLayoutManager.class, injector.getInstance(DashboardLayoutManagerSerializer.class));
      module.addSerializer(StringObject.class, injector.getInstance(StringObject.Serializer.class));
      module.addSerializer(BufferedImage.class, new BufferedImageSerializer());
      module.addSerializer(ImageIcon.class, new ImageIconSerializer());
      module.addSerializer(Date.class, new DateSerializer());
      module.addSerializer(BeakerDashboard.class, injector.getInstance(BeakerDashboard.Serializer.class));
      
      module.addSerializer(Color.class, injector.getInstance(ColorSerializer.class));
      module.addSerializer(GradientColor.class, injector.getInstance(GradientColorSerializer.class));
      module.addSerializer(XYChart.class, injector.getInstance(XYChartSerializer.class));
      module.addSerializer(CombinedPlot.class, injector.getInstance(CombinedPlotSerializer.class));
      module.addSerializer(Line.class, injector.getInstance(LineSerializer.class));
      module.addSerializer(Points.class, injector.getInstance(PointsSerializer.class));
      module.addSerializer(Bars.class, injector.getInstance(BarsSerializer.class));
      module.addSerializer(Stems.class, injector.getInstance(StemsSerializer.class));
      module.addSerializer(Area.class, injector.getInstance(AreaSerializer.class));
      module.addSerializer(YAxis.class, injector.getInstance(YAxisSerializer.class));
      module.addSerializer(Crosshair.class, injector.getInstance(CrosshairSerializer.class));
      module.addSerializer(LegendPosition.class, injector.getInstance(LegendPositionSerializer.class));
      module.addSerializer(Text.class, injector.getInstance(TextSerializer.class));
      module.addSerializer(ConstantLine.class, injector.getInstance(ConstantLineSerializer.class));
      module.addSerializer(ConstantBand.class, injector.getInstance(ConstantBandSerializer.class));

      module.addSerializer(EasyForm.class, injector.getInstance(EasyFormSerializer.class));
      module.addSerializer(TextField.class, injector.getInstance(TextFieldSerializer.class));
      module.addSerializer(TextArea.class, injector.getInstance(TextAreaSerializer.class));
      module.addSerializer(CheckBox.class, injector.getInstance(CheckBoxSerializer.class));
      module.addSerializer(ComboBox.class, injector.getInstance(ComboBoxSerializer.class));
      module.addSerializer(ListComponent.class, injector.getInstance(ListComponentSerializer.class));
      module.addSerializer(RadioButtonComponent.class, injector.getInstance(RadioButtonSerializer.class));
      module.addSerializer(CheckBoxGroup.class, injector.getInstance(CheckBoxGroupSerializer.class));
      module.addSerializer(DatePickerComponent.class, injector.getInstance(DatePickerComponentSerializer.class));
      module.addSerializer(ButtonComponent.class, injector.getInstance(ButtonComponentSerializer.class));
      module.addSerializer(LoadValuesButton.class, injector.getInstance(LoadValuesButtonSerializer.class));
      module.addSerializer(SaveValuesButton.class, injector.getInstance(SaveValuesButtonSerializer.class));

      module.addSerializer(CategoryBars.class, injector.getInstance(CategoryBarsSerializer.class));
      module.addSerializer(CategoryStems.class, injector.getInstance(CategoryStemsSerializer.class));
      module.addSerializer(CategoryPoints.class, injector.getInstance(CategoryPointsSerializer.class));
      module.addSerializer(CategoryLines.class, injector.getInstance(CategoryLinesSerializer.class));
      module.addSerializer(CategoryPlot.class, injector.getInstance(CategoryPlotSerializer.class));

      module.addSerializer(TreeMap.class, injector.getInstance(TreeMapSerializer.class));
      module.addSerializer(TreeMapNode.class, injector.getInstance(TreeMapNodeSerializer.class));

      module.addSerializer(Histogram.class, injector.getInstance(HistogramSerializer.class));

      module.addSerializer(HeatMap.class, injector.getInstance(HeatMapSerializer.class));

      mapper.registerModule(module);

      mapper.enable(SerializationFeature.INDENT_OUTPUT);

      // Manually serialize everything, either through mixin or serializer
      mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
      mapper.disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
      mapper.disable(MapperFeature.AUTO_DETECT_FIELDS);
      mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

      NamespaceClient.setInjector(injector);
    } catch(Exception e) {
      logger.error("exception while creating ObjectMapper", e);
    }
    return mapper;
  }

  @Provides
  @Singleton
  public JacksonJsonProvider getJackson(ObjectMapper mapper) {
    return new JacksonJsonProvider(mapper);
  }

}
