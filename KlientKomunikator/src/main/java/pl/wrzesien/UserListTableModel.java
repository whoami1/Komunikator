package pl.wrzesien;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-13.
 */
public class UserListTableModel extends AbstractTableModel
{

    private List<UserInfo> userInfoList;
    private String[] columnNames = new String[]{"Nick", "Status"};

    public UserListTableModel(List<UserInfo> userInfoList)
    {
        this.userInfoList = userInfoList;
    }

    @Override
    public int getRowCount()
    {

        return userInfoList.size();
    }

    @Override
    public int getColumnCount()
    {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {

        UserInfo userInfo = userInfoList.get(rowIndex);
        if (columnIndex == 0)
        {
            return userInfo.getUserNick();
        }
        return userInfo.getUserStatus();
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }
}
