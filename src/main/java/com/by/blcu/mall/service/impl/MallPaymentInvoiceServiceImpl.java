package com.by.blcu.mall.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.mall.service.IMallPaymentInvoiceService;
import com.by.blcu.mall.dao.IMallPaymentInvoiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mallPaymentInvoiceService")
public class MallPaymentInvoiceServiceImpl extends BaseServiceImpl implements IMallPaymentInvoiceService {
    @Autowired
    private IMallPaymentInvoiceDao mallPaymentInvoiceDao;

    @Override
    protected IBaseDao getDao() {
        return this.mallPaymentInvoiceDao;
    }
}