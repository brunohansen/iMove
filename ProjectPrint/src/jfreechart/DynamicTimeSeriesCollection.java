private long getX(RegularTimePeriod period) {
    long result = 0L;
    switch (position) {
        case (START) : result = period.getFirstMillisecond(workingCalendar); break;
        case (MIDDLE) : result = period.getMiddleMillisecond(workingCalendar); break;
        case (END) : result = period.getLastMillisecond(workingCalendar); break;
        default: result = period.getMiddleMillisecond(workingCalendar);
    }
    return result;
 }

private long getX(RegularTimePeriod period, int position, Calendar workingCalendar) {
	long result = 0L;
    switch (position) {
        case (START) : result = period.getFirstMillisecond(workingCalendar); break;
        case (MIDDLE) : result = period.getMiddleMillisecond(workingCalendar); break;
        case (END) : result = period.getLastMillisecond(workingCalendar); break;
        default: result = period.getMiddleMillisecond(workingCalendar);
    }
    return result;
}