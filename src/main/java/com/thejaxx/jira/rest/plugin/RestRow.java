package com.thejaxx.jira.rest.plugin;

/**
 * Created by Yoni Moses
 */
public class RestRow implements Comparable {
// ------------------------------ FIELDS ------------------------------

    private String value;
    private String key;

//	private Map<Integer, Object> m_numberToValues = new TreeMap<Integer, Object>();

// --------------------------- CONSTRUCTORS ---------------------------

    public RestRow(String key, String value) {
        this.value = value;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RestRow{" +
                "value='" + value + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    //	public RestRow()
//	{
//	}

// -------------------------- PUBLIC METHODS --------------------------

//	public long getRowNumber()
//	{
//		return m_rowNumber;
//	}
//
//	public void setRowNumber( long rowNumber )
//	{
//		m_rowNumber = rowNumber;
//	}
//
//	public void addDatabaseColumn( int columnNumber, Object value )
//	{
//		m_numberToValues.put(columnNumber, value );
//	}
//
//	public Object getValue( int columnNumber )
//	{
//		return m_numberToValues.get(columnNumber);
//	}
//
//	public int getNumberOfValues()
//	{
//		return m_numberToValues.keySet().size();
//	}
//
//	public String toString()
//	{
//		return "DatabaseRow[nr="+ m_rowNumber + ", values=" + m_numberToValues + "]";
//	}
//
// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface Comparable ---------------------

    public int compareTo(Object o) {
        int result;
        if (o instanceof RestRow) {
            RestRow otherRow = (RestRow) o;
            String rowValue = this.getValue();
            return rowValue.compareTo(otherRow.value);
        } else {
            result = 0;
        }
        return result;
    }
}
