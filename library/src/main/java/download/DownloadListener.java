package download;

import java.io.File;

/**
 *下载回调
 */
public interface DownloadListener {

    void onStartDownload();

    void onFinishDownload(File filepath);

    void onFail(String errorInfo);

    void onProgress(int i, long currentSize, long totalSize);
}
