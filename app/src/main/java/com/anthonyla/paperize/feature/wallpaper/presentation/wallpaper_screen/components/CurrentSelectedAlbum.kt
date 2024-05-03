package com.anthonyla.paperize.feature.wallpaper.presentation.wallpaper_screen.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.anthonyla.paperize.R
import com.anthonyla.paperize.feature.wallpaper.domain.model.SelectedAlbum
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

/**
 * The inner card for the album bottom sheet to represent each album
 */
@Composable
fun CurrentSelectedAlbum(
    selectedAlbum: SelectedAlbum?,
    onOpenBottomSheet: () -> Unit,
    onStop: () -> Unit,
    animate: Boolean
) {
    fun isValidUri(context: Context, uriString: String?): Boolean {
        val uri = uriString?.toUri()
        return try {
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(it)
                inputStream?.close()
            }
            true
        } catch (e: Exception) { false }
    }

    val showCoverUri = isValidUri(LocalContext.current, selectedAlbum?.album?.coverUri)

    ListItem(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onOpenBottomSheet() },
        headlineContent = {
            Text(
                text = selectedAlbum?.album?.displayedAlbumName ?: stringResource(R.string.no_album_selected),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            ) },
        supportingContent = {
            if (selectedAlbum != null) {
                Text(
                    text = "${selectedAlbum.wallpapers.size} ${stringResource(R.string.wallpaper)}",
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis
                )
            } },
        leadingContent = {
            Box (modifier = Modifier.size(60.dp)) {
                if (selectedAlbum != null) {
                    if (showCoverUri) {
                        GlideImage(
                            imageModel = { selectedAlbum.album.coverUri },
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.FillBounds,
                                alignment = Alignment.Center,
                            ),
                            requestBuilder = {
                                Glide
                                    .with(LocalContext.current)
                                    .asBitmap()
                                    .transition(BitmapTransitionOptions.withCrossFade())
                            },
                            loading = {
                                if (animate) {
                                    Box(modifier = Modifier.matchParentSize()) {
                                        CircularProgressIndicator(modifier = Modifier.align(
                                            Alignment.Center))
                                    }
                                }
                            },
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                } else {
                    Image(
                        imageVector = Icons.Default.RadioButtonUnchecked,
                        contentDescription = stringResource(R.string.no_icon),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        },
        trailingContent = {
            Row {
                if (selectedAlbum != null) {
                    IconButton(onClick = { onStop() }) {
                        Icon(imageVector = Icons.Default.Stop, contentDescription = stringResource(R.string.stop_the_album))
                    }
                }
                IconButton(onClick = { onOpenBottomSheet() }) {
                    Icon(
                        contentDescription = stringResource(R.string.click_to_select_a_different_album),
                        imageVector = Icons.Filled.ArrowDropDown,
                    )
                }
            }
        },
        tonalElevation = 10.dp
    )
}