require "fileutils"
require "nokogiri"

class String
  def underscore
    self.gsub(/::/, '/').
    gsub(/([A-Z]+)([A-Z][a-z])/,'\1_\2').
    gsub(/([a-z\d])([A-Z])/,'\1_\2').
    tr("-", "_").
    downcase
  end
end

TARGET_PATH = "betsy_tests"

FileUtils.rm_rf TARGET_PATH
FileUtils.mkdir TARGET_PATH

Dir.glob("rules/SA000*").each do |rule_path|

	rule_number = File.basename(rule_path).to_i
	rule_name = File.basename(rule_path)

  index = 0

	Dir.glob("#{rule_path}/**/*.bpel").each do |bpel|

	   	bpel_filename = rule_name + "-" + File.basename(bpel)
	   	bpel_name = bpel_filename[0..-6]
      index = index + 1
		target_bpel_path = File.join(TARGET_PATH, "sa-rules", rule_name, "#{rule_name}-#{index}")
		target_bpel_file_path = File.join(target_bpel_path, bpel_filename)

		FileUtils.mkdir_p target_bpel_path
	   	FileUtils.cp bpel, target_bpel_file_path

		bpel_doc = Nokogiri::XML(File.open(target_bpel_file_path))

		# uniquefy name and targetNamespace
		bpel_process = bpel_doc.at_xpath("/bpel:process","bpel" => "http://docs.oasis-open.org/wsbpel/2.0/process/executable")
		bpel_process[:name] = bpel_name
		bpel_process[:targetNamespace] = "http://dsg.wiai.uniba.de/betsy/rules/#{rule_name.downcase}/bpel/#{bpel_name}"

		# correct import location and filenaming
		bpel_imports = bpel_doc.xpath("//bpel:import","bpel" => "http://docs.oasis-open.org/wsbpel/2.0/process/executable")
		bpel_imports.each do |import|
			location = import[:location]
			location_filename = location.gsub("../","")

            location_filename = "TestInterface.wsdl" if location_filename.include? "TestInterface"
            location_filename = "TestPartner.wsdl" if location_filename.include? "TestPartner"

			FileUtils.cp File.join(rule_path,location), File.join(target_bpel_path,location_filename)

      if bpel_name == "SA00014-ImportRedefine" or bpel_name == "SA00014-WsdlImportRedefine"
        FileUtils.cp File.join(rule_path, "CalculatorSchema-Copy.xsd"), File.join(target_bpel_path, "CalculatorSchema-Copy.xsd")
      end

      if location_filename == "TestInterface.wsdl" or location_filename == "TestPartner.wsdl"

        wsdl_path = File.join(target_bpel_path,location_filename)
        puts wsdl_path
        wsdl_doc = Nokogiri::XML(File.open(wsdl_path))
        wsdl_imports = wsdl_doc.xpath("//wsdl:import", "wsdl" => "http://schemas.xmlsoap.org/wsdl/")
        puts "FOUND IMPORT" if wsdl_imports.size > 0
        wsdl_imports.each do |import_element|

          puts "ADDITIONAL FILE REQUIRED"

          wsdl_location = import_element[:location]

          FileUtils.cp File.join(rule_path, wsdl_location), File.join(target_bpel_path, File.basename(wsdl_location))
        end
      end

			import[:location] = location_filename
		end

		# write new bpel file
		File.open(target_bpel_file_path, 'w') {|f| f.write(bpel_doc.to_xml) }

	end

end