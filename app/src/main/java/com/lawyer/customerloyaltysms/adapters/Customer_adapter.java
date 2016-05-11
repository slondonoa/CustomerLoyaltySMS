package com.lawyer.customerloyaltysms.adapters;

/**
 * Created by stiven on 5/10/2016.
 */
public class Customer_adapter {

    public Customer_adapter(String idPerson,String allname,String cells,String document,String sent)
    {
        this.IdPerson=idPerson;
        this.allName=allname;
        this.Cells=cells;
        this.Document=document;
        this.Sent=sent;
    }

    public String IdPerson;
    public String allName;

    public String getCells() {
        return Cells;
    }

    public void setCells(String cells) {
        Cells = cells;
    }

    public String Cells;
    public String Document;
    public String Sent;

    public String getIdPerson() {
        return IdPerson;
    }

    public void setIdPerson(String idPerson) {
        IdPerson = idPerson;
    }

    public String getAllName() {
        return allName;
    }

    public void setAllName(String allname) {
        allName = allname;
    }

    public String getDocument() {
        return Document;
    }

    public void setDocument(String document) {
        Document = document;
    }

    public String getSent() {
        return Sent;
    }

    public void setSent(String sent) {
        Sent = sent;
    }


}
