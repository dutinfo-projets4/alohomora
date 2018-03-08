package alohomora.model;

public class Token {
    private int id;
    private String name;
    private String ip;
    private int loginTS;
    private int lastUpdateTS;

    public Token(int id, String name, String ip, int loginTS, int lastUpdateTS) {

        this.id = id;
        this.name = name;
        this.ip = ip;
        this.loginTS = loginTS;
        this.lastUpdateTS = lastUpdateTS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getLoginTS() {
        return loginTS;
    }

    public void setLoginTS(int loginTS) {
        this.loginTS = loginTS;
    }

    public int getLastUpdateTS() {
        return lastUpdateTS;
    }

    public void setLastUpdateTS(int lastUpdateTS) {
        this.lastUpdateTS = lastUpdateTS;
    }
}
