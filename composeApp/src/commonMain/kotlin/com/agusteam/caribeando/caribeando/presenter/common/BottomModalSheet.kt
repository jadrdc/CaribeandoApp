package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors

/**
 * The handoff's "BottomSheet": "Fills width, top radius 26, #FFFFFF, max height 88% of the
 * screen. 40x4 grabber, radius 2, #DDE2E6, centered, 10dp from the top. Scrolling body, then a
 * fixed footer with a 1dp #EEF0F1 top border, 14dp top / 22dp bottom padding. Scrim
 * rgba(0,48,73,.34), tap to dismiss."
 *
 * 2026-08-20 (design handoff pass): restyled in place -- top-only 26dp corners (was M3's default
 * shape), the exact grabber size/color/inset (was the generic `BottomSheetDefaults.DragHandle()`),
 * and the spec's scrim color/opacity. Signature unchanged so the existing caller (ExploreScreen's
 * filter sheet) keeps compiling. The spec's "max height 88%" and the footer's top-border treatment
 * are left to each sheet's own content (e.g. the Filtrar sheet, Cancelar reserva) to implement --
 * they're layout concerns of what's *inside* the sheet, not of this generic wrapper.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomModalSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    content: @Composable () -> Unit
) {
    val colors = caribeandoColors
    ModalBottomSheet(
        modifier = Modifier.wrapContentHeight(),
        sheetState = sheetState,
        shape = Radii.sheetShape,
        containerColor = colors.pageBackground,
        scrimColor = colors.scrim,
        onDismissRequest = {
            onDismiss()
        },
        dragHandle = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .background(colors.sheetGrabber, RoundedCornerShape(2.dp))
                )
            }
        }) {
        content()
    }
}
