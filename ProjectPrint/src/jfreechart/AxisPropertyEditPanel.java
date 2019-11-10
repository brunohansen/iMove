public void setAxisProperties(Axis axis) {
    axis.setLabel(getLabel());
    axis.setLabelFont(getLabelFont());
    axis.setLabelPaint(getLabelPaint());
    axis.setTickMarksVisible(isTickMarksVisible());
    // axis.setTickMarkStroke(getTickMarkStroke());
    axis.setTickLabelsVisible(isTickLabelsVisible());
    axis.setTickLabelFont(getTickLabelFont());
    axis.setTickLabelPaint(getTickLabelPaint());
    axis.setTickLabelInsets(getTickLabelInsets());
    axis.setLabelInsets(getLabelInsets());
}

public void setAxisProperties(AxisPropertyEditPanel axisPropertyEditPanel) {
    setLabel(axisPropertyEditPanel.getLabel());
    setLabelFont(axisPropertyEditPanel.getLabelFont());
    setLabelPaint(axisPropertyEditPanel.getLabelPaint());
    setTickMarksVisible(axisPropertyEditPanel.isTickMarksVisible());
    // axis.setTickMarkStroke(getTickMarkStroke());
    setTickLabelsVisible(axisPropertyEditPanel.isTickLabelsVisible());
    setTickLabelFont(axisPropertyEditPanel.getTickLabelFont());
    setTickLabelPaint(axisPropertyEditPanel.getTickLabelPaint());
    setTickLabelInsets(axisPropertyEditPanel.getTickLabelInsets());
    setLabelInsets(axisPropertyEditPanel.getLabelInsets());
}