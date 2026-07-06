package com.smartledger.app.di

import com.smartledger.app.data.repository.TransactionRepositoryImpl
import com.smartledger.app.domain.repository.ITransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 应用级依赖注入模块。
 * 将仓库接口绑定到实现类。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): ITransactionRepository
}
