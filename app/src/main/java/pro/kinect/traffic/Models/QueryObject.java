package pro.kinect.traffic.Models;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class QueryObject {
    private String _package;
    private String date;
    private String mobile_kb;
    private String name;
    private String reference_id;
    private String wifi_kb;

    public QueryObject(String _package, String date, String mobile_kb, String name, String reference_id, String wifi_kb) {
        this._package = _package;
        this.date = date;
        this.mobile_kb = mobile_kb;
        this.name = name;
        this.reference_id = reference_id;
        this.wifi_kb = wifi_kb;
    }
}
