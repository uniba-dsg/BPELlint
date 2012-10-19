# Checks the SA000XX.txt files for consistency
class DescriptionCheck
  attr_accessor :path, :dir, :filename

  def initialize(path)
    @path = path
    @filename = File.basename(path)
    @dir = File.dirname(path)
  end

  def check
    content = File.read(path)
    contents = content.split("@@@")

    # basic structure check
    if contents.size != 5
      puts "File #{path} does not have 4 @@@ separators"
      return
    end

    # check for any empty blocks
    if contents.any? {|c| c.strip.empty? }
      puts "File #{path} has empty block"
    end

    tests = contents[3].strip
    unless tests.split("\n").size.even?
      puts "File #{path} has odd lines of tests - should be even TEST/DESC pairs only"
    end

    # get testfiles
    testfiles = tests.split("\n").each_slice(2).map(&:first).map do |test|
      filename = test.gsub(":","")
      File.join(dir, filename)
    end

    # check that every file is a bpel file
    testfiles.each do |testfile|
      unless testfile.end_with? ".bpel"
        puts "Filename #{testfile} referenced from #{path} is no bpel file"
      end
    end

    # check file existence
    testfiles.each do |testfile|
      unless File.exists? testfile
        puts "Testfile #{testfile} referenced from #{path} does not exist"
      end
    end

    # check inverse
    Dir.glob("#{dir}/*.bpel") do |bpelfile|
      puts "#{bpelfile} not referenced in description #{path}" unless testfiles.include?(bpelfile)
    end

  end
end

Dir.glob("**/SA000*.txt") do |description_file|
  DescriptionCheck.new(description_file).check
end