def create_file(file_name, size_in_mb):
    """Creates a text file with the specified size in MB."""
    size_in_bytes = size_in_mb * 1024 * 1024  # Convert MB to bytes
    chunk = "A" * 1024  # 1 KB of data
    with open(file_name, "w") as f:
        for _ in range(size_in_bytes // 1024):  # Write chunks
            f.write(chunk)

# Create files of 1 MB, 100 MB, and 1 GB
create_file("1MB_file.txt", 1)
create_file("100MB_file.txt", 100)
create_file("1GB_file.txt", 1024)  # 1024 MB = 1 GB
