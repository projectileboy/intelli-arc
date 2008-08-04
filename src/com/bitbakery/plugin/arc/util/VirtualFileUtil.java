package com.bitbakery.plugin.arc.util;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Copied from the nice folks at JetBrains - this class is lifted from the Ruby plugin.
 */
public class VirtualFileUtil {
    @NonNls
    public static final char VFS_PATH_SEPARATOR = '/';

    /**
     * @param dir  directory
     * @param name file name
     * @return url for file with name in directory dir
     */
    public static String constructUrl(@NotNull final VirtualFile dir, final String name) {
        return dir.getUrl() + VFS_PATH_SEPARATOR + name;
    }

    /**
     * Searches files for list of urls
     *
     * @param urls Urls
     * @return found files or empty array
     */
    @NotNull
    public static VirtualFile[] getFiles(@NotNull final List<String> urls) {
        final VirtualFileManager virtualFileManager = VirtualFileManager.getInstance();
        final List<VirtualFile> files = new LinkedList<VirtualFile>();
        for (String url : urls) {
            if (TextUtil.isEmpty(url)) {
                continue;
            }
            final VirtualFile file = virtualFileManager.findFileByUrl(url);
            if (file != null) {
                files.add(file);
            }
        }
        return files.toArray(new VirtualFile[files.size()]);
    }

    /**
     * @param url Url for virtual file
     * @return file name with extension
     */
    @NotNull
    public static String getFileName(@NotNull final String url) {
        final int index = url.lastIndexOf(VFS_PATH_SEPARATOR);
        return index < 0 ? url : url.substring(index + 1);
    }

    /**
     * @param url Url for virtual file
     * @return url for parent directory of virtual file
     */
    @Nullable
    public static String getParentDir(@Nullable final String url) {
        if (url == null) {
            return null;
        }
        final int index = url.lastIndexOf(VFS_PATH_SEPARATOR);
        return index < 0 ? null : url.substring(0, index);
    }

    /**
     * @param fileName Virtual file name
     * @return file extension
     */
    @Nullable
    public static String getExtension(@NotNull final String fileName) {
        int index = fileName.lastIndexOf('.');
        return index < 0 ? null : fileName.substring(index + 1);
    }

    /**
     * @param fileName virtual file name
     * @return name without extension
     */
    @NotNull
    public static String removeExtension(@NotNull final String fileName) {
        int i = fileName.length() - 1;
        for (; i >= 0; i--) {
            if (fileName.charAt(i) == '.') {
                return fileName.substring(0, i);
            }
        }
        return fileName;
    }

    public static String constructLocalUrl(@NotNull final String path) {
        return VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, convertPathToVFSPath(path));
    }

    @NotNull
    public static String getNameWithoutExtension(@NotNull final String fileName) {
        int index = fileName.lastIndexOf('.');
        return index < 0 ? fileName : fileName.substring(0, index);
    }

    /**
     * For comparators. Compares files pathes.
     *
     * @param file1 file1
     * @param file2 file2
     * @return compare result
     */
    public static int compareVirtualFiles(@NotNull final VirtualFile file1,
                                          @NotNull final VirtualFile file2) {
        String path1 = file1.getPath();
        String path2 = file2.getPath();
        return path1.compareToIgnoreCase(path2);
    }

    public static boolean isValid(@Nullable final VirtualFile file) {
        return file != null && file.isValid();
    }

    public static String buildUrl(@NotNull final String rootUrl,
                                  @NotNull final String relativePath) {
        return rootUrl +
                //#TODO normalize
                (rootUrl.endsWith(String.valueOf(VFS_PATH_SEPARATOR))
                        ? ""
                        : VFS_PATH_SEPARATOR)
                + convertPathToVFSPath(relativePath);
    }

    @Nullable
    public static String pathToURL(final String path) {
        if (TextUtil.isEmpty(path)) {
            return null;
        }
        return constructLocalUrl(path);
    }

    public static VirtualFile findFileByLocalPath(@NotNull final String generateScriptPath) {
        return VirtualFileManager.getInstance().findFileByUrl(constructLocalUrl(generateScriptPath));
    }

    public static boolean fileExists(@Nullable final VirtualFile file) {
        return file != null && file.exists();
    }

    @Nullable
    public static String getRelativePath(@NotNull final String filePathOrUrl,
                                         @NotNull final String rootPathOrUrl) {
        if (filePathOrUrl.length() < rootPathOrUrl.length()) {
            return null;
        }
        String path = filePathOrUrl.substring(rootPathOrUrl.length());
        if (path.length() > 0 && path.charAt(0) == VFS_PATH_SEPARATOR) {
            path = path.substring(1);
        }
        return path;
    }

    public static class VirtualFilesComparator implements Comparator<VirtualFile> {
        public int compare(VirtualFile file1, VirtualFile file2) {
            return compareVirtualFiles(file1, file2);
        }
    }

    @NotNull
    public static String convertPathToVFSPath(@NotNull final String path) {
        return path.replace('\\', VFS_PATH_SEPARATOR);
    }

    @NotNull
    public static String convertToVFSPathAndNormalizeSlashes(@NotNull final String path) {
        final String newPath = convertPathToVFSPath(path);
        if (newPath.length() != 0 && newPath.charAt(newPath.length() - 1) == VFS_PATH_SEPARATOR) {
            return newPath.substring(0, newPath.length() - 1);
        }
        return newPath;
    }

    public static String convertToOSDependedPath(@NotNull final String path) {
        return path.replace(VFS_PATH_SEPARATOR, File.separatorChar);
    }

}
