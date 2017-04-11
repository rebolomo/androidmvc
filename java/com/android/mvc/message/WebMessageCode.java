package com.android.mvc.message;


/// <summary>
///	 representation of the web request.
/// </summary>
public enum WebMessageCode
{
	None(0),// = 0,
	//	account related
    METHOD_ACCOUNT_LOGIN(1),// + "api/" + API_VERSION + "/user/login";// + API_FILE_EXTENSION;
    METHOD_ACCOUNT_SIGNUP(2),// + "api/" + API_VERSION + "/user/register";// + API_FILE_EXTENSION;
    METHOD_ACCOUNT_AUTHORIZE(3),// + "api/" + API_VERSION + "/method/account.authorize" + API_FILE_EXTENSION;
    METHOD_ACCOUNT_LOGINBYFACEBOOK(4),// + "api/" + API_VERSION + "/method/account.signInByFacebook" + API_FILE_EXTENSION;
    METHOD_ACCOUNT_RECOVERY(5),// + "api/" + API_VERSION + "/method/account.recovery" + API_FILE_EXTENSION;
    METHOD_ACCOUNT_SETPASSWORD(6),// + "api/" + API_VERSION + "/method/account.setPassword" + API_FILE_EXTENSION;
    METHOD_ACCOUNT_CONNECT_TO_FACEBOOK(7),// + "api/" + API_VERSION + "/method/account.connectToFacebook" + API_FILE_EXTENSION;
    METHOD_ACCOUNT_DISCONNECT_FROM_FACEBOOK(8),// + "api/" + API_VERSION + "/method/account.disconnectFromFacebook" + API_FILE_EXTENSION;
    METHOD_ACCOUNT_LOGOUT(9),// + "api/" + API_VERSION + "/method/account.logOut" + API_FILE_EXTENSION;

    METHOD_APP_CHECKUSERNAME(20),// + "api/" + API_VERSION + "/method/com.mvc.checkUsername" + API_FILE_EXTENSION;
    METHOD_APP_TERMS(21),// + "api/" + API_VERSION + "/method/com.mvc.terms" + API_FILE_EXTENSION;
    METHOD_APP_THANKS(22),// + "api/" + API_VERSION + "/method/com.mvc.thanks" + API_FILE_EXTENSION;

    METHOD_ITEMS_REMOVE(30),// + "api/" + API_VERSION + "/method/items.remove" + API_FILE_EXTENSION;
    METHOD_ITEMS_NEW(31),// + "api/" + API_VERSION + "/method/items.new" + API_FILE_EXTENSION;
    METHOD_ITEMS_EDIT(32),// + "api/" + API_VERSION + "/method/items.edit" + API_FILE_EXTENSION;
    
    METHOD_ORDERS_SINGLE(40),// + "api/" + API_VERSION + "/order/";	//	GET
    METHOD_ORDERS_REMOVE(41),// + "api/" + API_VERSION + "/order/";	//	DELETE	
    METHOD_ORDERS_GET(42),// + "api/" + API_VERSION + "/method/items.new" + API_FILE_EXTENSION;
    METHOD_ORDERS_NEW(43),// + "api/" + API_VERSION + "/order";	//	POST
    METHOD_ORDERS_EDIT(44),// + "api/" + API_VERSION + "/order/";		//	PUT

    //METHOD_ORDERS_CHECK_DEFAULT(0),// + "api/" + API_VERSION + "/shop/default";	//	GET
    //METHOD_ORDERS_NEW(0),// + "api/" + API_VERSION + "/order";	//	POST
    
    METHOD_SHOPS_SINGLE(60),// + "api/" + API_VERSION + "/shop/";
    METHOD_SHOPS_REMOVE(61),// + "api/" + API_VERSION + "/shop/";
    METHOD_SHOPS_GET(62),// + "api/" + API_VERSION + "/shop";
    METHOD_SHOPS_NEW(63),// + "api/" + API_VERSION + "/shop";
    METHOD_SHOPS_EDIT(64),// + "api/" + API_VERSION + "/shop/";
    METHOD_SHOPS_ALL(65),// + "api/" + API_VERSION + "/shop/";
    
    METHOD_CUSTOMERS_ALL(80),// + "api/" + API_VERSION + "/customer/all";
    METHOD_CUSTOMERS_SINGLE(81),// + "api/" + API_VERSION + "/customer/";
    METHOD_CUSTOMERS_REMOVE(82),// + "api/" + API_VERSION + "/customer/";	
    METHOD_CUSTOMERS_NEW(83),// + "api/" + API_VERSION + "/customer";
    METHOD_CUSTOMERS_EDIT(84),// + "api/" + API_VERSION + "/customer/";
    METHOD_CUSTOMERS_SEARCH(85),// + "api/" + API_VERSION + "/customer/search";
    //METHOD_CUSTOMERS_GET(86),// + "api/" + API_VERSION + "/customer";
    
    METHOD_PRODUCTS_ALL(100),// + "api/" + API_VERSION + "/product/all";
    METHOD_PRODUCTS_SINGLE(101),// + "api/" + API_VERSION + "/product/";
    METHOD_PRODUCTS_REMOVE(102),// + "api/" + API_VERSION + "/product/";		
    //METHOD_PRODUCTS_GET(103),// + "api/" + API_VERSION + "/product";
    METHOD_PRODUCTS_NEW(104),// + "api/" + API_VERSION + "/product";
    METHOD_PRODUCTS_EDIT(105);// + "api/" + API_VERSION + "/product/";
    
    private int value = 0;

    private WebMessageCode(int value) {    
        this.value = value;
    }
}

