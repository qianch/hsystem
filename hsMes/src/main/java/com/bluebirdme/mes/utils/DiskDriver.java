/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package com.bluebirdme.mes.utils;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;

/**
 * @author Goofy
 * @Date 2018年4月10日 上午10:52:52
 */
public class DiskDriver {
    FileSystem fileSystem;
    FileSystemUsage fileSystemUsage;

    public DiskDriver() {
        super();
    }

    public DiskDriver(FileSystem fileSystem, FileSystemUsage fileSystemUsage) {
        super();
        this.fileSystem = fileSystem;
        this.fileSystemUsage = fileSystemUsage;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public FileSystemUsage getFileSystemUsage() {
        return fileSystemUsage;
    }

    public void setFileSystemUsage(FileSystemUsage fileSystemUsage) {
        this.fileSystemUsage = fileSystemUsage;
    }
}
