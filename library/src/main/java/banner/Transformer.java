package banner;

import android.support.v4.view.ViewPager.PageTransformer;

import banner.transformer.AccordionTransformer;
import banner.transformer.BackgroundToForegroundTransformer;
import banner.transformer.CubeInTransformer;
import banner.transformer.CubeOutTransformer;
import banner.transformer.DefaultTransformer;
import banner.transformer.DepthPageTransformer;
import banner.transformer.FlipHorizontalTransformer;
import banner.transformer.FlipVerticalTransformer;
import banner.transformer.ForegroundToBackgroundTransformer;
import banner.transformer.RotateDownTransformer;
import banner.transformer.RotateUpTransformer;
import banner.transformer.ScaleInOutTransformer;
import banner.transformer.StackTransformer;
import banner.transformer.TabletTransformer;
import banner.transformer.ZoomInTransformer;
import banner.transformer.ZoomOutSlideTransformer;
import banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
