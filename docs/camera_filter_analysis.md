# Camera Screen Video Recording with Filters

## Technical Stack Overview

The camera feature relies on the following components:

- **Jetpack Compose** – used for the UI layer to build composables such as `CameraScreen` and `FilterBottomSheet`.
- **CameraView (Otaliastudios)** – third‑party camera library located under `feature/cameracapture`. It supports real‑time filters through OpenGL.
- **CameraMedia module** – Compose wrappers around `CameraView` for the recording screen. Implementation is in `feature/cameramedia`.
- **mp4compose** – included in `feature/filter` for OpenGL filters and potential post‑processing of recorded videos.

## Recording Flow

1. **Navigation** – `AppNavHost` includes `cameraMediaNavGraph`, which registers `CameraMediaScreen` at route `DestinationRoute.CAMERA_ROUTE`【F:app/src/main/java/com/puskal/tiktokcompose/navigation/AppNavHost.kt†L24-L36】.
2. **CameraMediaScreen** – hosts a pager with multiple tabs. When the camera tab is active it shows `CameraScreen` which renders the actual camera preview【F:feature/cameramedia/src/main/java/com/puskal/cameramedia/CameraMediaScreen.kt†L26-L70】.
3. **Camera Preview Setup** – inside `CameraScreen`, `CameraPreview` creates a `CameraView` and attaches lifecycle, default facing and recording mode. Preview is displayed using `AndroidView` so Compose can host the native view【F:feature/cameramedia/src/main/java/com/puskal/cameramedia/tabs/CameraScreen.kt†L228-L301】.
4. **Selecting Filters** – `FilterBottomSheet` lists values from `Filters` and updates `selectedFilter` when the user taps one. `CameraPreview` observes `selectedFilter` via `LaunchedEffect` and calls `cameraView.filter = selectedFilter.newInstance()` to apply the filter in real time【F:feature/cameramedia/src/main/java/com/puskal/cameramedia/tabs/CameraScreen.kt†L238-L250】【F:feature/cameramedia/src/main/java/com/puskal/cameramedia/filter/FilterBottomSheet.kt†L18-L50】.
5. **Recording Video** – the capture button triggers `cameraView.takeVideo(file)` when the current capture option is a video mode. Once recording stops, `CameraView` notifies through `CameraListener.onVideoTaken`【F:feature/cameramedia/src/main/java/com/puskal/cameramedia/tabs/CameraScreen.kt†L292-L318】.
6. **Real‑Time Filter Pipeline** – `CameraView` internally checks that preview type is `GL_SURFACE` and applies the provided `Filter` implementation to every frame, so both the preview and resulting video contain the selected effect. Relevant methods are `setFilter(Filter)` and `getFilter()` in `CameraView`【F:feature/cameracapture/src/main/java/com/otaliastudios/cameraview/CameraView.java†L2686-L2734】.

## Performance Considerations

- Filtering occurs on the GPU via OpenGL shaders provided by `CameraView`. Using a single filter is inexpensive, generally maintaining realtime frame rates (60 fps) on modern devices.
- Applying multiple filters simultaneously can be achieved via `MultiFilter`, but increases GPU load and memory usage (as documented in the library docs)【F:feature/cameracapture/docs/_docs/filters.md†L94-L119】.
- Additional processing libraries (e.g., `mp4compose`) are available for offline filter application. `CameraGlPreviewView` shows a minimal example of connecting CameraX frames to an OpenGL pipeline, which can be used when more complex effects or custom filters are required【F:feature/cameramedia/src/main/java/com/puskal/cameramedia/gl/CameraGlPreviewView.kt†L1-L99】.

## Summary

When the user opens the camera screen, a `CameraView` instance is created and displayed through Compose. A bottom sheet provides a list of filters from the `Filters` enumeration. Selecting a filter triggers `cameraView.filter = selectedFilter.newInstance()`, enabling real‑time GPU processing of the preview stream. Videos recorded through `CameraView.takeVideo()` include the applied filter since frames are filtered before encoding. For more advanced scenarios or post‑processing, the project includes the `mp4compose` library and a sample `CameraGlPreviewView` demonstrating how to connect CameraX with OpenGL filters.
