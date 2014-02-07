Dir.glob("sa_tests/**/*.bpel").each do |bpel_file|

  folder = File.basename(bpel_file)
  puts "{\"Testcases/bpel2owfn/#{bpel_file}\", \"#{folder[4..-1].to_i}\"},"

end