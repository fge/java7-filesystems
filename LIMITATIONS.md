## Introduction

This file documents the limitations of file systems currently implemented using
this package.

As the library progresses, some of these limitations may be removed/lessened.

## General limitations

### Directory watch service not supported

This means that the following methods will always throw an
`UnsupportedOperationException`:

* `Path`'s `.register()` (which, in fact, it inherits from the `Watchable`
interface);
* `FileSystem`'s `.newWatchService()`.

## Path limitations

### No symbolic link support

This affects several items:

* `Path`'s `.toRealPath()` method defaults to calling `.toAbsolutePath()`;
* `BasicFileAttributes`'s `.isSymbolicLink()` always returns `false`;
* `LinkOption...` arguments to `Files.readAttributes()` and others are ignored;
* consequently, such arguments are also ignored in all copy/open/move operations
(recall that `LinkOption` implements `OpenOption` and `CopyOption`).

### Only Unix-like paths are supported

Among other consequences:

* the name separator is always `/`;
* paths are absolute if and only if they have a root (and the root is always 
`/`, too); see `Path.isAbsolute()`, `Path.getRoot()`);
* the "current directory" name token and "parent" name token are hardcoded to 
`.` and `..` respectively.

### Cross filesystem path resolution/relativization is not supported

In theory, you can resolve/relativize a `Path` against another `Path` if both
paths share the same filesystem _provider_ (if this is not the case, a 
`ProviderMismatchException` is thrown).

This package restricts it a step further: paths cannot be resolved/relativized
against one another if they do not share the same _filesystem_. Any attempt to
do so will throw a `FileSystemMismatchException` (which extends
`ProviderMismatchException`).

**NOTE NOTE NOTE!!** This also affects `.compareTo()`.

### Default filesystem implementation not supported

This means that `Path.toFile()` will always throws an
`UnsupportedOperationException`.

## File store limitations

A `FileStore` is an abstract representation of a storage for filesystem entries.
A `FileStore` may refer to any type of storage, physical or not. Typically,
under Linux, there is one file store per mount point for the default filesystem.

### File store attributes are not supported

This means that method `getFileStoreAttributeView()` will always return null,
and that `getAttribute()` will always throw an `UnsupportedOperationException`.

## Filesystem limitations

### Only one file store per filesystem

Theoretically, a single `FileSystem` may have several file stores; this API only
supports one file store.

The consequences are as follows:

* the filesystem is read only if its only underlying file store is also read
only;
* the `getFileStores()` method returns a singleton `Set` with only the defined
file store (using `Collections.singleton()`).

### No watch service supported

See above.

### No user principal lookup service supported

This means that an implementation's `.getUserPrincipalLookupService()` will
always throw an `UnsupportedOperationException`.

### No `PathMatcher` support (VIOLATION OF CONTRACT!)

In theory, all filesystem implementations should at least support the `glob`
pattern matching `PathMatcher`; unfortunately, yours truly does not know of a
currently widely available, free implementation available.

And yes, this violates the JSR 203 API contract. Sorry...

## FileSystemProvider limitations

### Only one file store per filesystem, redux

A `FileSystemProvider` has a method called `getFileStore()` which takes a `Path`
as an argument; but since, with the current design, a `FileSystem` only has one
`FileStore`, the implementation of this method performs the following:

```
    return path.getFileSystem   ().getFileStores().iterator().next();
```
### No support for `ByteChannel`s

This means that `Files.newByteChannel()`, `FileChannel.open()` etc will all
throw an `UnsupportedOperationException`. Only classic `InputStream` and
`OutputStream`s are currently supported.

### No support for symbolic links, redux

This means that `.isSameFile()` will return true if and only if the absolute
representation of both paths are `.equals()`. Also, all specific symbolic link
operations in the provider throw `UnsupportedOperationException`.

### No support for "hard links", either

Therefore `.createLink()` also throws `UnsupportedOperationException`.

### No support for "hidden" paths...

This is due to a strangeness in the JSR 203 API. For some reason, there is _one_
method in this API to tell whether a given `Path` is supposed to be hidden with
regards to the API and it is located in... `FileSystemProvider`. This method is
called `isHidden()`.

In this API, this method will **always** return **false**.

## File attribute view limitations

### Only basic file attribute views supported

This is only a temporary limitation, which will be removed later.
