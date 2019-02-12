JDeodorant-Impedindo Movimento

com...ConnectionHandler::commit(Connection):void->com...MicroDataOutputStream com...ResultSetHandler::writeRow(ResultSet):void->com...MicroDataOutputStream

class ConnectionHandler {
	...
	public void createStatement(Connection connection)
	public void prepareStatement(Connection connection)
	public void rollback(Connection connection)
	public void commit(Connection connection) {
	    try {
	        connection.commit();
	        out_.writeInt(1);
	        out_.flush();
	    } catch (SQLException e) {
	    	service_.handleException(e);
	    }
	}
	...
}

class ResultSetHandler {
	...
	public void close(ResultSet rs)
	public void updateRow(ResultSet rs)
	public void writeRow(ResultSet rs)
	public void deleteRow(ResultSet rs) {
		try	{
			rs.deleteRow();
			out_.writeInt(1);
			out_.flush();
		} catch (SQLException e) {
			service_.handleException(e);
		}
	}
	...
}

class MicroDataOutputStream {
	...
	public void flush()
	public void writeBoolean(boolean b)
	public void writeByte(byte b)
	public void writeBytes(byte[] b)
	public void writeDouble(double d)
	...
}
