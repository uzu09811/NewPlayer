/* NewPlayer
 *
 * @author Christian Schabesberger
 *
 * Copyright (C) NewPipe e.V. 2024 <code(at)newpipe-ev.de>
 *
 * NewPlayer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPlayer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPlayer.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.newpipe.newplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.newpipe.newplayer.model.VideoPlayerViewModel
import net.newpipe.newplayer.model.VideoPlayerViewModelImpl
import net.newpipe.newplayer.ui.VideoPlayerControllerUI
import net.newpipe.newplayer.ui.VideoPlayerUI
import net.newpipe.newplayer.ui.theme.VideoPlayerTheme

@AndroidEntryPoint
class PlayerFragment() : Fragment() {

    private val viewModel: VideoPlayerViewModel by viewModels<VideoPlayerViewModelImpl>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = activity?.window!!
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val view = inflater.inflate(R.layout.player_framgent, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.player_copose_view)
        val frameView = view.findViewById<FrameLayout>(R.id.frame_layout)

        if (viewModel.uiState.value.fullscreen) {
            println("gurken fragment created for fullscreen")
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
        }

        viewModel.listener = object : VideoPlayerViewModel.Listener {
            override fun requestUpdateLayoutRatio(ratio: Float) {
                frameView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    dimensionRatio = "$ratio:1"
                }
            }

            override fun switchToFullscreen() {
                println("gurken fullscreen")
            }
        }

        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                VideoPlayerTheme {
                    VideoPlayerUI(viewModel)
                }
            }
        }
        return view
    }
}