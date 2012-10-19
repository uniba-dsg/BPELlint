class TestDataGenerator

  def self.generate(testdir)
    Dir.glob("#{testdir}/**/*.bpel").map do |file|
      dirname = File.basename(File.dirname(file))
      sa_number = dirname[2..-1].to_i
      "{\"#{file}\", \"\"},"
    end
  end
end

result = TestDataGenerator.generate("betsy")
puts "Total: #{result.size}"
puts result