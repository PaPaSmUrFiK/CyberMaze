package com.cybermaze.core.di

import com.cybermaze.core.game.system.CollectibleSystem
import com.cybermaze.core.game.system.CollisionSystem
import com.cybermaze.core.game.system.EnemySystem
import com.cybermaze.core.game.system.MovementSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for game systems.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideMovementSystem(): MovementSystem {
        return MovementSystem()
    }
    
    @Provides
    @Singleton
    fun provideCollisionSystem(): CollisionSystem {
        return CollisionSystem()
    }
    
    @Provides
    @Singleton
    fun provideEnemySystem(movementSystem: MovementSystem): EnemySystem {
        return EnemySystem(movementSystem)
    }
    
    @Provides
    @Singleton
    fun provideCollectibleSystem(): CollectibleSystem {
        return CollectibleSystem()
    }
}
