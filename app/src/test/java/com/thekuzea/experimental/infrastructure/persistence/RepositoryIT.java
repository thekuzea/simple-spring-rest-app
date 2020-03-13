package com.thekuzea.experimental.infrastructure.persistence;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import net.ttddyy.dsproxy.asserts.ProxyTestDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.thekuzea.experimental.config.PersistenceConfig;
import com.thekuzea.experimental.config.dbunit.DbUnitConfig;
import com.thekuzea.experimental.config.dbunit.ProxyDataSourceConfig;

@SpringBootTest(classes = {
        PersistenceConfig.class,
        DbUnitConfig.class,
        ProxyDataSourceConfig.class
})
@ActiveProfiles("dbunit")
@Transactional
@AutoConfigureTestEntityManager
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DbUnitConfiguration(databaseConnection = "dbUnitDatabaseConnection")
public abstract class RepositoryIT {

    @Autowired
    protected ProxyTestDataSource dataSource;

    @Autowired
    protected TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        dataSource.reset();
    }
}
