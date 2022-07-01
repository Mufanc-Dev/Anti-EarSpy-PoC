package android.view;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import dev.rikka.tools.refine.RefineAs;

@RefineAs(SurfaceControl.class)
@SuppressLint("ParcelCreator")
public class SurfaceControlHidden implements Parcelable {
    public static IBinder createDisplay(String name, boolean secure) {
        throw new RuntimeException("Stub!");
    }

    public static void openTransaction() {
        throw new RuntimeException("Stub!");
    }

    public static void closeTransaction() {
        throw new RuntimeException("Stub!");
    }

    public static void setDisplaySurface(IBinder displayToken, Surface surface) {
        throw new RuntimeException("Stub!");
    }

    public static void setDisplayProjection(
            IBinder displayToken,
            int orientation,
            Rect layerStackRect,
            Rect displayRect
    ) {
        throw new RuntimeException("Stub!");
    }

    public static void setDisplayLayerStack(IBinder displayToken, int layerStack) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        throw new RuntimeException("Stub!");
    }
}
