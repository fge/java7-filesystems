package com.github.fge.fs.api.driver;

import java.nio.file.CopyOption;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DefaultFileSystemOptionsChecker
    implements FileSystemOptionsChecker
{
    private static final Set<OpenOption> DEFAULT_READ_OPTIONS;
    private static final Set<OpenOption> DEFAULT_WRITE_OPTIONS;

    static {
        final Set<OpenOption> set = new HashSet<>();

        set.add(StandardOpenOption.READ);

        DEFAULT_READ_OPTIONS = new HashSet<>(set);

        set.clear();

        set.add(StandardOpenOption.WRITE);
        set.add(StandardOpenOption.CREATE);

        DEFAULT_WRITE_OPTIONS = new HashSet<>(set);
    }

    private static final Set<CopyOption> DEFAULT_COPY_OPTIONS;

    static {
        DEFAULT_COPY_OPTIONS = Collections.emptySet();
    }

    private final Set<OpenOption> readOptions = new HashSet<>();
    private final Set<OpenOption> writeOptions = new HashSet<>();

    private final Set<CopyOption> copyOptions = new HashSet<>();

    private final Map<CopyOption, OpenOption> copyToReadOptions
        = new HashMap<>();
    private final Map<CopyOption, OpenOption> copyToWriteOptions
        = new HashMap<>();

    public DefaultFileSystemOptionsChecker()
    {
        readOptions.add(StandardOpenOption.READ);

        writeOptions.add(StandardOpenOption.CREATE);
        writeOptions.add(StandardOpenOption.WRITE);
    }

    protected final void addReadOption(final OpenOption option)
    {
        Objects.requireNonNull(option);
        if (!readOptions.add(option))
            throw new IllegalArgumentException("option " + option
                + " is already registered");
    }

    protected final void addWriteOption(final OpenOption option)
    {
        Objects.requireNonNull(option);
        if (!writeOptions.add(option))
            throw new IllegalArgumentException("option " + option
                + " is already registered");
    }

    protected final void addCopyOption(final CopyOption option)
    {
        Objects.requireNonNull(option);

        if (!copyOptions.add(option))
            throw new IllegalArgumentException("option " + option
                + " is already registered");
    }

    protected final void addCopyToReadOption(final CopyOption copyOption,
        final OpenOption openOption)
    {
        Objects.requireNonNull(copyOption);
        Objects.requireNonNull(openOption);

        if (copyToReadOptions.put(copyOption, openOption) != null)
            throw new IllegalArgumentException("option " + copyOption
                + " is already registered");
    }

    protected final void addCopyToWriteOption(final CopyOption copyOption,
        final OpenOption openOption)
    {
        Objects.requireNonNull(copyOption);
        Objects.requireNonNull(openOption);

        if (copyToWriteOptions.put(copyOption, openOption) != null)
            throw new IllegalArgumentException("option " + copyOption
                + " is already registered");
    }

    @Override
    public final Set<OpenOption> getReadOptions(final OpenOption... options)
    {
        final Set<OpenOption> set = new HashSet<>();

        for (final OpenOption option: options) {
            if (!readOptions.contains(option))
                throw new UnsupportedOperationException(option
                    + " not supported");
            set.add(option);
        }

        if (set.isEmpty())
            set.addAll(DEFAULT_READ_OPTIONS);

        return set;
    }

    @Override
    public final Set<OpenOption> getWriteOptions(final OpenOption... options)
    {
        final Set<OpenOption> set = new HashSet<>();

        for (final OpenOption option: options) {
            if (!writeOptions.contains(option))
                throw new UnsupportedOperationException(option
                    + " not supported");
            set.add(option);
        }

        if (set.isEmpty())
            set.addAll(DEFAULT_WRITE_OPTIONS);

        return set;
    }

    @Override
    public final Set<CopyOption> getCopyOptions(final CopyOption... options)
    {
        final Set<CopyOption> set = new HashSet<>(options.length);

        for (final CopyOption option: copyOptions) {
            if (!copyOptions.contains(option))
                throw new UnsupportedOperationException(option
                    + " not supported");
            set.add(option);
        }

        if (set.isEmpty())
            set.addAll(DEFAULT_COPY_OPTIONS);

        return set;
    }

    @Override
    public final Set<OpenOption> copyToReadOptions(final CopyOption... options)
    {
        final Set<OpenOption> set = new HashSet<>();

        OpenOption openOption;

        for (final CopyOption copyOption: options) {
            openOption = copyToReadOptions.get(copyOption);
            if (openOption == null)
                throw new UnsupportedOperationException(copyOption
                    + " not supported");
            set.add(openOption);
        }

        if (set.isEmpty())
            set.addAll(DEFAULT_READ_OPTIONS);

        return set;
    }

    @Override
    public final Set<OpenOption> copyToWriteOptions(final CopyOption... options)
    {
        final Set<OpenOption> set = new HashSet<>();

        OpenOption openOption;

        for (final CopyOption copyOption: options) {
            openOption = copyToWriteOptions.get(copyOption);
            if (openOption == null)
                throw new UnsupportedOperationException(copyOption
                    + " not supported");
            set.add(openOption);
        }

        if (set.isEmpty())
            set.addAll(DEFAULT_WRITE_OPTIONS);

        return set;
    }
}
