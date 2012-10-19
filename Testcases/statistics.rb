# Prints statistics about how many tests are used and what rules are already implemented.
class ProgressStatistics

  MISSING_RULES = [47]

  attr_accessor :implemented, :sa_tests

  def initialize
    @implemented = []
    @sa_tests = []
  end

  def print
    # output
    puts "Implemented #{implemented.size}/#{total_rules} (#{implemented_in_percent}%) with #{sa_tests.flatten.size} SA tests and #{betsy_tests.size} betsy tests"
    # implemented.each_slice(10) {|a| puts a.inspect}
    #puts "Unimplemented #{unimplemented.size}/94"
    #unimplemented.each_slice(10) {|a| puts a.inspect}
  end

  def implemented_in_percent
    (implemented.size.to_f * 100/total_rules).to_i
  end

  def total_rules
    95 - MISSING_RULES.size
  end

  def unimplemented
   (1..95).to_a - MISSING_RULES - implemented
  end

  def add_implemented_rule(file)
    implemented << File.basename(file)[2..(file.length - 4)].to_i
    sa_tests << Dir.glob(File.join(File.dirname(file), "*.bpel"))
  end

  def betsy_tests
    Dir.glob("betsy/**/*.bpel")
  end

end


# computation
$ps = ProgressStatistics.new
Dir.glob("c:/projects/isabel/TestCases/**/SA*.txt") do |file|
  $ps.add_implemented_rule file
end
$ps.print

