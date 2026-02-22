package com.kuro.notiflow.presentation.settings.ui.data_management

import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.use_case.ClearAllNotificationsUseCase
import com.kuro.notiflow.domain.use_case.ExportNotificationsUseCase
import com.kuro.notiflow.domain.use_case.ImportNotificationsUseCase
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateSettingsUseCase
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.presentation.common.utils.SnackBarType
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.settings.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DataManagementViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        loadSettingsUseCase: LoadSettingsUseCase = mockk(),
        clearAllNotificationsUseCase: ClearAllNotificationsUseCase = mockk(),
        updateSettingsUseCase: UpdateSettingsUseCase = mockk(),
        exportNotificationsUseCase: ExportNotificationsUseCase = mockk(),
        importNotificationsUseCase: ImportNotificationsUseCase = mockk()
    ): DataManagementViewModel {
        every { loadSettingsUseCase.invoke() } returns flowOf(SettingsModel())
        return DataManagementViewModel(
            clearAllNotificationsUseCase = clearAllNotificationsUseCase,
            loadSettingsUseCase = loadSettingsUseCase,
            updateSettingsUseCase = updateSettingsUseCase,
            exportNotificationsUseCase = exportNotificationsUseCase,
            importNotificationsUseCase = importNotificationsUseCase
        )
    }

    @Test
    fun `onImportClick emits request import event`() = runTest {
        val viewModel = createViewModel()
        val eventDeferred = async { viewModel.events.first() }

        viewModel.onImportClick()

        val event = eventDeferred.await()
        assertTrue(event is DataManagementEvent.RequestImport)
    }

    @Test
    fun `onImportData emits success snack bar`() = runTest {
        val importUseCase = mockk<ImportNotificationsUseCase>()
        coEvery { importUseCase.invoke("content://import") } returns Result.success(2)

        val viewModel = createViewModel(importNotificationsUseCase = importUseCase)
        val eventDeferred = async { viewModel.events.first() }

        viewModel.onImportData("content://import")

        val event = eventDeferred.await()
        val snackEvent = event as DataManagementEvent.ShowSnackBar
        assertEquals(R.string.data_management_import_success, snackEvent.messageResId)
        assertEquals(listOf(2), snackEvent.formatArgs)
        assertEquals(SnackBarType.SUCCESS, snackEvent.type)
    }

    @Test
    fun `onImportData emits failed snack bar when import fails`() = runTest {
        val importUseCase = mockk<ImportNotificationsUseCase>()
        coEvery { importUseCase.invoke(any()) } returns Result.failure(IllegalStateException("fail"))

        val viewModel = createViewModel(importNotificationsUseCase = importUseCase)
        val eventDeferred = async { viewModel.events.first() }

        viewModel.onImportData("content://import")

        val event = eventDeferred.await()
        val snackEvent = event as DataManagementEvent.ShowSnackBar
        assertEquals(R.string.data_management_import_failed, snackEvent.messageResId)
        assertEquals(SnackBarType.ERROR, snackEvent.type)
    }

    @Test
    fun `onExportClick emits request export with file name`() = runTest {
        val viewModel = createViewModel()
        val eventDeferred = async { viewModel.events.first() }

        viewModel.onExportClick()

        val event = eventDeferred.await()
        val exportEvent = event as DataManagementEvent.RequestExport
        assertTrue(exportEvent.fileName.startsWith(Constants.Export.BASE_FILE_NAME))
        assertTrue(exportEvent.fileName.endsWith(".${Constants.Export.FILE_EXTENSION}"))
    }

    @Test
    fun `onExportData emits failed snack bar when export fails`() = runTest {
        val exportUseCase = mockk<ExportNotificationsUseCase>()
        coEvery { exportUseCase.invoke(any(), any()) } returns Result.failure(IllegalStateException("fail"))

        val viewModel = createViewModel(exportNotificationsUseCase = exportUseCase)
        val eventDeferred = async { viewModel.events.first() }

        viewModel.onExportData("content://export")

        val event = eventDeferred.await()
        val snackEvent = event as DataManagementEvent.ShowSnackBar
        assertEquals(R.string.data_management_export_failed, snackEvent.messageResId)
        assertEquals(SnackBarType.ERROR, snackEvent.type)
    }

    @Test
    fun `onExportData emits success snack bar when export succeeds`() = runTest {
        val exportUseCase = mockk<ExportNotificationsUseCase>()
        coEvery { exportUseCase.invoke(any(), any()) } returns Result.success(
            com.kuro.notiflow.domain.models.export.ExportResult(
                uriString = "content://export",
                fileName = "file.csv",
                totalCount = 1
            )
        )

        val viewModel = createViewModel(exportNotificationsUseCase = exportUseCase)
        val eventDeferred = async { viewModel.events.first() }

        viewModel.onExportData("content://export")

        val event = eventDeferred.await()
        val snackEvent = event as DataManagementEvent.ShowSnackBar
        assertEquals(R.string.data_management_export_success, snackEvent.messageResId)
        assertEquals(listOf("file.csv"), snackEvent.formatArgs)
        assertEquals(SnackBarType.SUCCESS, snackEvent.type)
    }

    @Test
    fun `onRetentionClick shows dialog`() = runTest {
        val viewModel = createViewModel()

        viewModel.onRetentionClick()

        assertTrue(viewModel.state.value.isRetentionDialogVisible)
    }

    @Test
    fun `onRetentionDialogModeChanged updates mode`() = runTest {
        val viewModel = createViewModel()

        viewModel.onRetentionDialogModeChanged(RetentionMode.ALWAYS)

        assertEquals(RetentionMode.ALWAYS, viewModel.state.value.dialogRetentionMode)
    }

    @Test
    fun `onRetentionDialogDaysChanged clamps to max`() = runTest {
        val viewModel = createViewModel()

        viewModel.onRetentionDialogDaysChanged(999)

        assertEquals(Constants.Settings.MAX_RETENTION_DAYS, viewModel.state.value.dialogRetentionDays)
    }

    @Test
    fun `onRetentionDialogCancel hides dialog and resets days`() = runTest {
        val viewModel = createViewModel()
        viewModel.onRetentionClick()
        viewModel.onRetentionDialogDaysChanged(10)

        viewModel.onRetentionDialogCancel()

        assertEquals(false, viewModel.state.value.isRetentionDialogVisible)
        assertEquals(Constants.Settings.DEFAULT_RETENTION_DAYS, viewModel.state.value.dialogRetentionDays)
    }

    @Test
    fun `onRetentionDialogConfirm updates settings and hides dialog`() = runTest {
        val updateUseCase = mockk<UpdateSettingsUseCase>()
        coEvery { updateUseCase.invoke(any()) } returns Result.success(1)

        val viewModel = createViewModel(updateSettingsUseCase = updateUseCase)
        viewModel.onRetentionDialogModeChanged(RetentionMode.CUSTOM)
        viewModel.onRetentionDialogDaysChanged(10)

        viewModel.onRetentionDialogConfirm()

        assertEquals(10, viewModel.state.value.retentionDays)
        assertEquals(false, viewModel.state.value.isRetentionDialogVisible)
        coVerify(timeout = 1_000) { updateUseCase.invoke(match { it.dataRetentionDays == 10 }) }
    }

    @Test
    fun `onClearData emits success snack bar`() = runTest {
        val clearUseCase = mockk<ClearAllNotificationsUseCase>()
        coEvery { clearUseCase.invoke() } returns Unit

        val viewModel = createViewModel(clearAllNotificationsUseCase = clearUseCase)
        val eventDeferred = async { viewModel.events.first() }

        viewModel.onClearData()

        val event = eventDeferred.await()
        val snackEvent = event as DataManagementEvent.ShowSnackBar
        assertEquals(R.string.data_management_clear_success, snackEvent.messageResId)
        assertEquals(SnackBarType.SUCCESS, snackEvent.type)
    }

    @Test
    fun `onClearData emits failed snack bar when clear fails`() = runTest {
        val clearUseCase = mockk<ClearAllNotificationsUseCase>()
        coEvery { clearUseCase.invoke() } throws IllegalStateException("fail")

        val viewModel = createViewModel(clearAllNotificationsUseCase = clearUseCase)
        val eventDeferred = async { viewModel.events.first() }

        viewModel.onClearData()

        val event = eventDeferred.await()
        val snackEvent = event as DataManagementEvent.ShowSnackBar
        assertEquals(R.string.data_management_clear_failed, snackEvent.messageResId)
        assertEquals(SnackBarType.ERROR, snackEvent.type)
    }
}
