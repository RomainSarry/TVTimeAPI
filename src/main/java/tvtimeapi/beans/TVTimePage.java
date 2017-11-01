package tvtimeapi.beans;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimePage {
    public TVTimePage(Document htmlDom) {
        this.htmlDom = htmlDom;
    }

    Document htmlDom;

    public Document getHtmlDom() {
        return htmlDom;
    }

    public void setHtmlDom(Document htmlDom) {
        this.htmlDom = htmlDom;
    }

    public Elements getFields(String selector) {
        return htmlDom.select(selector);
    }

    public Element getField(String selector) {
        return getFields(selector).first();
    }

    public Element getLastField(String selector) {
        return getFields(selector).last();
    }
}
