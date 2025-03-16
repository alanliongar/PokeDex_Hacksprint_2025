package com.example.pokedex.list

import com.example.pokedex_hacksprint_2025.list.data.PokemonListRepository
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel
import android.content.Context
import android.util.Log
import com.example.pokedex_hacksprint_2025.common.data.model.Pokemon
import com.example.pokedex_hacksprint_2025.list.presentation.ui.PokeListUiState
import com.example.pokedex_hacksprint_2025.list.presentation.ui.PokemonUiData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PokeListViewModelTestMockWithoutTurbine {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
    private val repository: PokemonListRepository = mock()
    private val context: Context = mock()

    private lateinit var mockedLog: MockedStatic<Log>

    @Before
    fun setupMockLog() {
        mockedLog = Mockito.mockStatic(Log::class.java)
        mockedLog.`when`<Any> { Log.d(Mockito.anyString(), Mockito.anyString()) }.thenReturn(0)
        mockedLog.`when`<Any> { Log.e(Mockito.anyString(), Mockito.anyString()) }.thenReturn(0)
    }

    @After
    fun tearDownMockLog() {
        mockedLog.close() // Libera o mock após cada teste
    }

    private val underTest by lazy {
        PokeListViewModel(
            repository = repository,
            coroutineDispatcher = testDispatcher,
        )
    }

    @Test
    fun `Given fresh viewmodel When collecting pokemon list Then asset isloading state`() {
        runTest {
            //Given
            val pokemon = emptyList<Pokemon>()
            whenever(repository.getPokemonList()).thenReturn(Result.success(pokemon))

            //When
            var result: PokeListUiState? = null
            backgroundScope.launch(testDispatcher) {
                result = underTest.pokemonListUiState.drop(0).first()
            }

            //Then
            val expected = PokeListUiState(isLoading = true)
            assertEquals(expected, result)
        }
    }

    @Test
    fun `Given fresh viewmodel When collecting API result with data Then update local data and return it`() {
        runTest {
            // Given
            val pokemon = listOf(
                Pokemon(
                    name = "um",
                    image = "dois"
                )
            )
            whenever(repository.getPokemonList()).thenReturn(Result.success(pokemon))

            val expected = PokeListUiState(
                pokemonList = listOf(
                    PokemonUiData(
                        name = pokemon[0].name,
                        image = pokemon[0].image
                    )
                ), errorMessage = "Success"
            )
            // When
            var result: PokeListUiState? = null
            backgroundScope.launch(testDispatcher) {
                result = underTest.pokemonListUiState.drop(1).first()
            }
            assertEquals(expected, result)
        }
    }

    @Test
    fun `Given fresh viewmodel When collecting API result without data Then (try to) update local data and return it`() {
        runTest {
            // Given
            val pokemon: List<Pokemon>? = null
            whenever(repository.getPokemonList()).thenReturn(Result.success(pokemon))

            val expected = PokeListUiState(
                pokemonList = emptyList(),
                isLoading = false,
                isError = true,
                errorMessage = "Empty request"
            )
            // When
            var result: PokeListUiState? = null
            backgroundScope.launch(testDispatcher) {
                result =
                    underTest.pokemonListUiState.drop(1)
                        .first() //drop the first state(isloading true)
            }
            assertEquals(expected, result)
        }
    }
}