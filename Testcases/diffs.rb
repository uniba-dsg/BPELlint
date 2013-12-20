# requires xmllint and diff to be in the PATH

class Test

  BPELVAL_DIR = "rules"
  BETSY_DIR = "betsy"
  OPTIONS = "--ignore-space-change --ignore-blank-lines -U9999"

  def self.add_diff_stats(diff_file)
    @@diff_stats ||= []

    add_lines=`cat #{diff_file} | grep ^+ | wc -l`.strip.to_i
    remove_lines=`cat #{diff_file} | grep ^- | wc -l`.strip.to_i
    @@diff_stats << [diff_file, add_lines + remove_lines, add_lines, remove_lines]
  end

  def self.print_diff_stats
    File.open("diffs/diff_stats.csv","w") do |f|
      @@diff_stats.sort {|a,b| a[1] <=> b[1] }.each do |ds|
        f << ds.join(";") + "\n"
      end
    end
  end

  def self.compute_wsdl_diffs
    main_wsdl = "#{BETSY_DIR}/TestInterface.wsdl"
    partner_wsdl = "#{BETSY_DIR}/TestPartner.wsdl"

    Dir.glob("#{BPELVAL_DIR}/**/*.wsdl").each do |file|
      name = File.read(file).scan(/name="([^"\\.]*)"/)[0][0]
      if name == "TestInterface"
        wsdl = main_wsdl
      elsif name == "TestPartner"
        wsdl = partner_wsdl
      else
        raise "should not happen"
      end
      dirname = File.basename(File.dirname(file))
      filename = File.basename(file)
      diff_file = "diffs/#{dirname}_WSDL_#{filename}.diff"
      `xmllint --c14n #{wsdl} > origin.xml`
      `xmllint --c14n #{file} > current.xml`
      `diff #{OPTIONS} origin.xml current.xml > #{diff_file}`
      add_diff_stats(diff_file)
    end
  end

  def self.compute_bpel_diffs
    Dir.glob("#{BPELVAL_DIR}/**/*.bpel").each do |file|
      name = File.read(file).scan(/name="([^"\\.]*)"/)[0][0]
      origin = Dir.glob("#{BETSY_DIR}/**/#{name}.bpel")[0]

      unless origin
        puts "ERROR - Origin could not be found for #{name} with file #{file}"
        next
      end

      dirname = File.basename(File.dirname(file))
      filename = File.basename(file)
      diff_file = "diffs/#{dirname}_BPEL_#{filename}.diff"
      `xmllint --c14n #{origin} > origin.xml`
      `xmllint --c14n #{file} > current.xml`
      `diff #{OPTIONS} origin.xml current.xml > #{diff_file}`
      add_diff_stats(diff_file)
    end
  end
end

require "fileutils"
FileUtils.rm_rf "diffs"
Dir.mkdir "diffs"
Test.compute_wsdl_diffs
puts "------"
Test.compute_bpel_diffs
Test.print_diff_stats
FileUtils.rm "origin.xml"
FileUtils.rm "current.xml"