JDeodorant

com...ConnectionHandler::commit(Connection):void	com...MicroDataOutputStream
com...ConnectionHandler::rollback(Connection):void	com...MicroDataOutputStream
com...ResultSetHandler::afterLast(ResultSet):void	com...MicroDataOutputStream
com...ResultSetHandler::beforeFirst(ResultSet):void	com...MicroDataOutputStream
com...ResultSetHandler::deleteRow(ResultSet):void	com...MicroDataOutputStream
com...ResultSetHandler::isAfterLast(ResultSet):void	com...MicroDataOutputStream
com...ResultSetHandler::isBeforeFirst(ResultSet):void	com...MicroDataOutputStream
com...ResultSetHandler::isFirst(ResultSet):void	com...MicroDataOutputStream
com...ResultSetHandler::isLast(ResultSet):void	com...MicroDataOutputStream
com...ResultSetHandler::writeRow(ResultSet):void	com...MicroDataOutputStream

CAMC e IUC não permitem

class ConnectionHandler
{
	...
    public void createStatement(Connection connection)
    public void prepareStatement(Connection connection)
    public void setAutoCommit(Connection connection)
    public void setTransactionIsolation(Connection connection)
    public void commit(Connection connection) throws IOException
    {
        try
        {
            connection.commit();
            out_.writeInt(1);
            out_.flush();
        }
        catch (SQLException e)
        {
            service_.handleException(e);
        }
    }
    public void rollback(Connection connection)
    ...
}


class ResultSetHandler
{
	...
    public void close(ResultSet rs)
    public void deleteRow(ResultSet rs) throws IOException
    {
        try
        {
            rs.deleteRow();
            out_.writeInt(1); 
            out_.flush();
        }
        catch (SQLException e)
        {
            service_.handleException(e);
        }
    }
    public void isAfterLast(ResultSet rs)
    public void isLast(ResultSet rs)
    public void updateRow(ResultSet rs)
    protected void writeRow(ResultSet rs)
    ...
}

class MicroDataOutputStream 
{
	...
    public void flush()
    public void writeBoolean(boolean b)
    public void writeByte(byte b)
    public void writeBytes(byte[] b)
    public void writeDouble(double d)
    public void writeFloat(float f)
    public void writeInt(int i)
    public void writeLong(long l)
    public void writeShort(short s)
    public void writeString(String s)
    public void writeUTF(String s)
    ...
}
