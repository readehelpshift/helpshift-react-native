
Pod::Spec.new do |s|
  s.name         = "RNHelpshift"
  s.version      = "1.0.0"
  s.summary      = "RNHelpshift"
  s.description  = <<-DESC
                  RNHelpshift
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNHelpshift.git", :tag => "master" }
  s.source_files  = "RNHelpshift/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"

  # Pods for Helpshift
  s.dependency "Helpshift", "7.5.1-withCampaigns"
  # s.dependency "Helpshift", "7.5.1"

end

  