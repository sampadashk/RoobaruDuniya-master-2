package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 17/12/17.
 */

public class RoobaruGlobaLProvider {
    public static RoobaruGlobaLProvider roobaruGlobaLProvider;
    private static User u;
    private RoobaruGlobaLProvider(User u)
    {
        this.u=u;

    }
    public static RoobaruGlobaLProvider getUserRoobaru( User u)

    {
        if(roobaruGlobaLProvider!=null)
        {
            roobaruGlobaLProvider=new RoobaruGlobaLProvider(u);
        }
        return  roobaruGlobaLProvider;

    }
    public static User getUser()
    {
        return u;
    }
}
