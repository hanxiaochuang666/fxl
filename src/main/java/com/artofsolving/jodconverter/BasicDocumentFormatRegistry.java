package com.artofsolving.jodconverter;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicDocumentFormatRegistry implements DocumentFormatRegistry {
    private List documentFormats = new ArrayList();
    public BasicDocumentFormatRegistry() {
    }
    public void addDocumentFormat(DocumentFormat documentFormat) {
        this.documentFormats.add( documentFormat );
    }
    protected List getDocumentFormats() {
        return this.documentFormats;
    }
    @Override
    public DocumentFormat getFormatByFileExtension(String extension) {
        if (extension == null) {
            return null;
        } else {
            if (extension.contains("doc")) {
                extension = "doc";
            }
            if (extension.contains("ppt")) {
                extension = "ppt";
            }
            if (extension.contains("xls")) {
                extension = "xls";
            }
            String lowerExtension = extension.toLowerCase();
            Iterator it = this.documentFormats.iterator();
            DocumentFormat format;
            do {
                if (!it.hasNext()) {
                    return null;
                }
                format = (DocumentFormat) it.next();
            } while (!format.getFileExtension().equals( lowerExtension ));

            return format;
        }
    }
    @Override
    public DocumentFormat getFormatByMimeType(String mimeType) {
        Iterator it = this.documentFormats.iterator();
        DocumentFormat format;
        do {
            if (!it.hasNext()) {
                return null;
            }
            format = (DocumentFormat) it.next();
        } while (!format.getMimeType().equals( mimeType ));
        return format;
    }
}