protected Date nextStandardDate(Date date, DateTickUnit unit) {
    Date previous = previousStandardDate(date, unit);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(previous);
    calendar.add(unit.getCalendarField(), unit.getCount());
    return calendar.getTime();

}